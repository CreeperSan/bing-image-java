package com.creepersan.bingimage.config.type

enum class BingImageConfigLocation(value: Int) {

    ChinaMainland(0),

    England(1),

    Japan(2);

    fun toFormattedString():String {
        return when(this){
            ChinaMainland -> "中国大陆"
            England -> "英国"
            Japan -> "日本"
        }
    }
}