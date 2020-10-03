package com.creepersan.bingimage.task

import com.creepersan.bingimage.config.ConfigUtils
import com.creepersan.bingimage.const.BingImageResponseType
import com.creepersan.bingimage.const.BingResolution
import com.creepersan.bingimage.lib.BeanConvert
import com.creepersan.bingimage.lib.database.factory.DatabaseFactory
import com.creepersan.bingimage.lib.file.FileUtils
import com.creepersan.bingimage.lib.network.NetworkManager
import com.creepersan.bingimage.lib.network.bean.BingResponseImagesBean
import com.creepersan.bingimage.lib.network.url.UrlBuilder
import com.creepersan.bingimage.log.LogFactory
import com.creepersan.bingimage.log.Logger
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.scheduling.annotation.Scheduled

@Configuration
@EnableScheduling
class FetchBingImageFromServerTask {

    companion object{
        const val TAG = "AutoPullBingImageTask"
    }

    @Scheduled(cron = "0 5 0/12 * * *")
    fun fetchBingImageData(){
        LogFactory.log(TAG, "准备拉取最新的必应图片")
        Observable.create<BingResponseImagesBean> { emitter ->
            NetworkManager.fetchBingImageData(UrlBuilder.getBingApiUrl(BingImageResponseType.JSON, ConfigUtils.getBingImageConfig().bingImageLoadCount))
                    .observeOn(Schedulers.single())
                    .subscribe({ response ->
                        val bean = response.first

                        LogFactory.log(TAG, "共${bean.images.size}张图片", Logger.Level.Debug)
                        bean.images.forEach { imageBean ->
                            emitter.onNext(imageBean)
                        }
                        emitter.onComplete()
                    }, { exception ->
                        throw exception
                    })
        }.flatMap { imageBean ->
            return@flatMap Observable.create<Any> { emitter ->
                Observable.create<BingResolution> { innerEmitter ->
                    LogFactory.log(TAG, "【${imageBean.startdate}】开始处理", Logger.Level.Debug)
                    BingResolution.allResolution().forEach{ resolution ->
                        innerEmitter.onNext(resolution)
                    }
                    innerEmitter.onComplete()
                }.flatMap { resolution ->
                    return@flatMap Observable.create<List<Any>> { innerInnerEmitter ->
                        NetworkManager.fetchBingImageFile(UrlBuilder.getBingImageUrl(imageBean.urlbase, resolution)).subscribe({ imageByteData ->
                            LogFactory.log(TAG, "【${imageBean.startdate}】${resolution.name} 正在下载", Logger.Level.Debug)
                            FileUtils.saveImage(imageByteData, imageBean.formattedYear(), imageBean.formattedMonth(), imageBean.formattedDay(), resolution)
                            LogFactory.log(TAG, "【${imageBean.startdate}】${resolution.name} 下载完成", Logger.Level.Debug)
                            innerInnerEmitter.onNext(listOf(imageBean, resolution))
                            innerInnerEmitter.onComplete()
                        }, { exception ->
                            innerInnerEmitter.onError(exception)
                        })
                        innerInnerEmitter.onNext(listOf(imageBean, resolution))
                    }
                }.toList().subscribe({  itemList ->
                    DatabaseFactory.getDatabaseOperator(ConfigUtils.getBingImageConfig().databaseType).insertOrUpdateBingImage(
                            BeanConvert.bingImageResponseImageBean2DatabaseBingImageBean(imageBean)
                    )
                    LogFactory.log(TAG, "【${imageBean.startdate} 数据已添加至数据库", Logger.Level.Debug)
                    emitter.onNext(imageBean)
                    emitter.onComplete()
                }, {
                    emitter.onError(it)
                })
            }
        }.toList().subscribe({ resp ->
            LogFactory.log(TAG, "拉取最新必应图片已完成")
            LogFactory.log(TAG, resp.toString(), Logger.Level.Debug)
        }, {
            LogFactory.log(TAG, "自动化拉取必应图片失败，exception=${it.message}", Logger.Level.Warming)
            throw it
        })

    }

}