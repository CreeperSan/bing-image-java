package com.creepersan.bingimage

import com.creepersan.bingimage.const.BingImageResponseType
import com.creepersan.bingimage.const.RESOLUTION_ARRAY
import com.creepersan.bingimage.lib.file.FileUtils
import com.creepersan.bingimage.lib.network.NetworkManager
import com.creepersan.bingimage.lib.network.url.UrlBuilder
import com.sun.jndi.ldap.Connection
import org.springframework.boot.CommandLineRunner
import org.springframework.stereotype.Component

@Component
class BingImageInitializer: CommandLineRunner {
    override fun run(vararg args: String?) {
//        NetworkManager.fetchBingImageData(UrlBuilder.getBingApiUrl(BingImageResponseType.JSON, 7))
//                .subscribe({
//                    val bean = it.first
//                    val rawMsg = it.second
//                    bean.images.forEach { imageBean ->
//                        val year = imageBean.year()
//                        val month = imageBean.month()
//                        val day = imageBean.day()
//                        RESOLUTION_ARRAY.forEach { resolution ->
//                            val url = UrlBuilder.getBingImageUrl(imageBean.urlbase, resolution)
//                            NetworkManager.fetchBingImageFile(url, year, month, day, resolution).subscribe({ imageDatas ->
//                                val imageFile = FileUtils.saveImage(imageDatas, year, month, day, resolution)
//                                println("保存图片成功 ${imageFile.absolutePath}")
//                            }, { exception ->
//                                println("下载必应搜索背景图片失败，日期 ${imageBean.fullstartdate} 分辨率 ${resolution.value} exception=${exception.message}")
//                            })
//                        }
//                    }
//                }, { exception ->
//                    println("获取网络请求时发生异常")
//                    println(exception.message)
//                })
    }
}