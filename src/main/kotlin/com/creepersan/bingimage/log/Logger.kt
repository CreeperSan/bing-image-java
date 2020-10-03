package com.creepersan.bingimage.log

interface Logger {

    enum class Level{
        Debug,
        Info,
        Warming,
        Error,
    }

    fun log(level: Level, content: String)

}