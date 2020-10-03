package com.creepersan.bingimage.lib.network.url

import com.creepersan.bingimage.config.type.BingImageConfigLoadCount
import com.creepersan.bingimage.const.BingImageResponseType
import com.creepersan.bingimage.const.BingResolution

object UrlBuilder {

    fun getBingApiUrl(format: BingImageResponseType, getCount: BingImageConfigLoadCount): String{
        return "https://cn.bing.com/HPImageArchive.aspx?format=${format.value}&idx=0&n=${getCount.value}"
    }

    fun getBingImageUrl(imgPart: String, resolution: BingResolution): String{
        val resolutionUrl = when(resolution){
            BingResolution.L_1920_1200     -> "1920x1200"
            BingResolution.L_1920_1080     -> "1920x1080"
            BingResolution.L_1366_768      -> "1366x768"
            BingResolution.L_1280_720      -> "1280x720"
            BingResolution.L_1024_768      -> "1024x768"
            BingResolution.L_800_600       -> "800x600"
            BingResolution.L_800_480       -> "800x480"
            BingResolution.L_640_480       -> "640x480"
            BingResolution.L_400_240       -> "400x240"
            BingResolution.L_320_240       -> "320x240"
            BingResolution.P_1080_1920     -> "1080x1920"
            BingResolution.P_768_1366      -> "768x1366"
            BingResolution.P_768_1280      -> "768x1280"
            BingResolution.P_720_1280      -> "720x1280"
            BingResolution.P_480_800       -> "480x800"
            BingResolution.P_480_640       -> "480x640"
            BingResolution.P_240_400       -> "240x400"
            BingResolution.P_240_320       -> "240x320"
        }
        return "https://www.bing.com${imgPart}_${resolutionUrl}.jpg"
    }


}