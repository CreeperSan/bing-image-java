package com.creepersan.bingimage.log

import com.creepersan.bingimage.lib.file.FileUtils
import java.sql.Connection
import java.sql.DriverManager

internal class SQLiteLogger: Logger{

    init {
        // 初始化表
//        val connection = getConnection()
    }

//    private fun getConnection(): Connection {
//        return DriverManager.getConnection("jdbc:sqlite:${FileUtils.logcatFolder.absolutePath}/log.db")
//    }

    override fun log(level: Logger.Level, content: String) {
        println("【${level.name}】$content")
    }

}