package com.creepersan.bingimage.config.bean

data class MySQLConnectionConfig (
        var hostAddress: String = "",               // 服务器地址
        var databaseName: String = "",              // 数据表名称
        var databaseUsername: String = "",          // 连接用户名
        var databasePassword: String = ""           // 连接密码
) : BaseConfig()