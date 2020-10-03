package com.creepersan.bingimage.lib.database.bean

class BingImageDatabaseBean(
        val _id: Int = 0,
        var year: Int = 0,
        var month: Int = 0,
        var day: Int = 0,
        var copyright: String = "",
        var author: String = "",
        var path: String = "",
        var title: String = "",
        var location: String = "",
        var json: String = ""
){

    fun month(): String = if (month < 10) "0$month" else month.toString()

    fun day(): String = if (day < 10) "0$day" else day.toString()

}