package com.creepersan.bingimage.lib.network.bean

import com.creepersan.bingimage.config.type.BingImageConfigLocation
import com.creepersan.bingimage.log.LogFactory
import com.creepersan.bingimage.log.Logger
import java.util.ArrayList

data class BingResponseBean(
        var images: List<BingResponseImagesBean> = ArrayList(),
        var tooltips: BingResponseTooltipsBean = BingResponseTooltipsBean()
)

/**
 * copyright字段有点特殊
 *
 * 节日： 【今日七夕】Love locker on a red stripe hanging in a temple in Chengdu, China © Philippe LEJEANVRE/Getty Images
 */
data class BingResponseImagesBean(
        var startdate: String = "",
        var fullstartdate: String = "",
        var enddate: String = "",
        var url: String = "",
        val urlbase: String = "",
        var copyright: String = "",
        var copyrightlink: String = "",
        var title: String = "",
        var quiz: String = "",
        var wp: Boolean = true,
        var hsh: String = "",
        var drk: Int = 0,
        var top: Int = 0,
        var bot: Int = 0,
        var hs: List<Any> = ArrayList()
){
    fun formattedYear(): Int{
        return if (fullstartdate.length == 12){
            fullstartdate.substring(0, 4).toIntOrNull() ?: 0
        }else{
            0
        }
    }

    fun formattedMonth(): Int{
        return if (fullstartdate.length == 12){
            fullstartdate.substring(4, 6).toIntOrNull() ?: 0
        }else{
            0
        }
    }

    fun formattedDay(): Int{
        return if (fullstartdate.length == 12){
            fullstartdate.substring(6, 8).toIntOrNull() ?: 0
        }else{
            0
        }
    }

    fun formattedTitle(location: BingImageConfigLocation): String{
        try {
            return when(location){
                // 中国内地
                BingImageConfigLocation.ChinaMainland -> {
                    if (copyright.contains("【")){
                        // 特殊日子
                        if (copyright.contains("©")){
                            copyright.substring(0, copyright.indexOf("©"))
                        } else {
                            copyright
                        }
                    } else {
                        // 普通日子
                        if (copyright.indexOf('，') > 0){
                            copyright.substring(0, copyright.indexOf('，'))
                        } else {
                            copyright.substring(0, copyright.indexOf('('))
                        }
                    }
                }
                // 日本
                BingImageConfigLocation.Japan -> {
                    copyright.substring(copyright.indexOf('｢')+1, copyright.indexOf('｣'));
                }
                // 英国
                BingImageConfigLocation.England -> {
                    copyright.substring(0, copyright.indexOf(','));
                }
            }
        } catch (e: Exception) {
            LogFactory.getLogger().log(Logger.Level.Warming, "解析标题出错 copyright=$copyright     message=${e.message}")
            return copyright
        }
    }

    fun formattedAuthor(location: BingImageConfigLocation): String{
        try {
            return when(location){
                // 中国内地
                BingImageConfigLocation.ChinaMainland -> {
                    if (copyright.contains("【")){
                        if (copyright.contains("©")){
                            copyright.substring(copyright.indexOf("©"))
                        } else {
                            ""
                        }
                    } else {
                        copyright.substring(copyright.indexOf('(')+1, copyright.indexOf(')'))
                    }
                }
                // 日本
                BingImageConfigLocation.Japan -> {
                    copyright.substring(copyright.indexOf('(')+1, copyright.indexOf(')'))
                }
                // 英国
                BingImageConfigLocation.England -> {
                    copyright.substring(copyright.indexOf('(')+1, copyright.indexOf(')'))
                }
            }
        } catch (e: Exception) {
            LogFactory.getLogger().log(Logger.Level.Warming, "解析作者出错 copyright=$copyright    message=${e.message}")
            return ""
        }
    }

    // 彩岩国家湖滨区的岩洞，密歇根 (© Kenneth Keifer/Getty Images)
    fun formattedLocation(location: BingImageConfigLocation): String{
        try {
            return when(location){
                BingImageConfigLocation.ChinaMainland -> {
                    if (copyright.indexOf('，') >= 0) // 有地址
                        copyright.substring( copyright.indexOf('，') + 1, copyright.lastIndexOf('(')).replace(" ","")
                    else // 没地址
                        ""
                }
                BingImageConfigLocation.Japan -> {
                    copyright.substring(copyright.indexOf('｣') + 1, copyright.indexOf('('))
                }
                BingImageConfigLocation.England -> {
                    if (copyright.indexOf(',') >= 0)
                        copyright.substring( copyright.indexOf(',') + 1, copyright.lastIndexOf('(')).replace(" ", "")
                    else
                        ""
                }
            }
        } catch (e: Exception) {
            LogFactory.getLogger().log(Logger.Level.Warming, "解析地址出错 copyright=$copyright     message=${e.message}")
            return ""
        }
    }

    fun formattedPath(): String{
        return "https://cn.bing.com$url"
    }

}

data class BingResponseTooltipsBean(
        var loading: String = "",
        var previous: String = "",
        var next: String = "",
        var walle: String = "",
        var walls: String = ""
)