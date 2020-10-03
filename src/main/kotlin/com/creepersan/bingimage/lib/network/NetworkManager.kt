package com.creepersan.bingimage.lib.network

import com.creepersan.bingimage.comm.TwoValues
import com.creepersan.bingimage.const.BingResolution
import com.creepersan.bingimage.lib.file.FileUtils
import com.creepersan.bingimage.lib.json.JsonUtils
import com.creepersan.bingimage.lib.network.bean.BingResponseBean
import com.creepersan.bingimage.lib.network.exception.BingImageParseException
import com.creepersan.bingimage.lib.network.exception.FetchBingImageErrorException
import com.google.gson.Gson
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.schedulers.Schedulers
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.File
import java.io.FileOutputStream

object NetworkManager {
    private lateinit var mClient: OkHttpClient

    fun init(){
        mClient = OkHttpClient()
    }

    /**
     * 获取必应搜索服务器上的背景图片
     * @param url 要请求的链接，从[UrlBuilder]获取地址
     */
    fun fetchBingImageData(url: String): Observable<TwoValues<BingResponseBean, String>>{
        return Observable.just(url)
                .subscribeOn(Schedulers.io())
                .map { urlRequest ->
                    Request.Builder().get().url(urlRequest).build()
                }
                .map { request ->
                    mClient.newCall(request).execute()
                }
                .map {  response ->
                    response.body()?.string() ?: throw FetchBingImageErrorException("从必应服务器上获取必应图片链接失败")
                }
                .map { responseString ->
                    val bean = JsonUtils.parseJson(responseString, BingResponseBean::class.java) ?: throw BingImageParseException("解析必应服务器返回数据失败")
                    TwoValues(bean , responseString)
                }
    }

    fun fetchBingImageFile(url: String): Observable<ByteArray>{
        return Observable.just(url)
                .subscribeOn(Schedulers.single())
                .map { picUrl ->
                    Request.Builder().get().url(picUrl).build()
                }
                .map { request ->
                    mClient.newCall(request).execute()
                }
                .map { response ->
                    response.body()?.byteStream()?.readBytes() ?: throw FetchBingImageErrorException("从必应服务器上获取必应图片文件失败")
                }
    }

}