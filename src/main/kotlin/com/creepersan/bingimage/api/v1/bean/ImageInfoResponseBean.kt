package com.creepersan.bingimage.api.v1.bean

data class ImageInfoResponseBean(
        var data: Int = 0,
        var year: Int = 0,
        var month: Int = 0,
        var day: Int = 0,
        var title: String = "",
        var location: String = "",
        var author: String = "",
        var img_url: String = "",
        var img_url_thumbnail: String = "",
        var img_url_base: String
)