package com.creepersan.bingimage.config.bean

data class SQLiteConnectionConfig (
        var databaseFileName: String = "bing_image.db"       // 数据库名称
) : BaseConfig()