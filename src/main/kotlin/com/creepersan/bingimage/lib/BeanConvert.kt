package com.creepersan.bingimage.lib

import com.creepersan.bingimage.api.v1.bean.ImageInfoResponseBean
import com.creepersan.bingimage.config.ConfigUtils
import com.creepersan.bingimage.lib.database.bean.BingImageDatabaseBean
import com.creepersan.bingimage.lib.json.JsonUtils
import com.creepersan.bingimage.lib.network.bean.BingResponseImagesBean

object BeanConvert {

    /**
     * 必应服务器接口返回的数据转换成数据库存储的数据
     */
    fun bingImageResponseImageBean2DatabaseBingImageBean(bean: BingResponseImagesBean): BingImageDatabaseBean {
        return BingImageDatabaseBean(
                _id = bean.fullstartdate.substring(0, 8).toInt(),
                year = bean.formattedYear(),
                month = bean.formattedMonth(),
                day = bean.formattedDay(),
                copyright = bean.copyright,
                author = bean.formattedAuthor(ConfigUtils.getBingImageConfig().bingImageLocation),
                path = bean.formattedPath(),
                title = bean.formattedTitle(ConfigUtils.getBingImageConfig().bingImageLocation),
                location = bean.formattedLocation(ConfigUtils.getBingImageConfig().bingImageLocation),
                json = JsonUtils.toString(bean)
        )
    }

    /**
     * 数据库存储的数据转换成自己API返回的数据（V1版本）
     */
    fun bingImageDatabaseBean2ResponseBean(bean: BingImageDatabaseBean): ImageInfoResponseBean{
        return ImageInfoResponseBean(
                data = bean._id,
                year = bean.year,
                month = bean.month,
                day = bean.day,
                title = bean.title,
                location = bean.location,
                author = bean.author,
                img_url = "/bing-image/${bean.year}/${bean.month()}/${bean.day()}/1920x1080.jpg",
                img_url_thumbnail = "/bing-image/${bean.year}/${bean.month()}/${bean.day()}/400x240.jpg",
                img_url_base = "/bing-image/${bean.year}/${bean.month()}/${bean.day()}"
        )
    }

}