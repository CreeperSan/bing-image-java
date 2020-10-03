package com.creepersan.bingimage.utils

import java.text.SimpleDateFormat

object TypeConvertUtils {
    private val timestampToBackupFileFormatter by lazy { SimpleDateFormat("yyyyMMdd_HHmmss_SSS") }

    fun intTo2LengthString(num: Int): String{
        return if (num >= 10){
            "${num%100/10}${num%10}"
        }else{
            "0${num%10}"
        }
    }

    fun timestampToBackupFilePostfix(timestamp: Long): String{
        return timestampToBackupFileFormatter.format(timestamp)
    }

}