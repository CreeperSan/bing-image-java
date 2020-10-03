package com.creepersan.bingimage.log

object LogFactory {
    private lateinit var logger : Logger

    fun getLogger(): Logger{
        if (!this::logger.isInitialized){
            logger = SQLiteLogger()
        }
        return logger
    }

    fun log(tag: String = "", content: String, level: Logger.Level = Logger.Level.Info){
        getLogger().log(level, "${if (tag.isEmpty()) "" else "【$tag】"}$content")
    }

}