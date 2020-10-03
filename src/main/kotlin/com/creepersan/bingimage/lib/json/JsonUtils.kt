package com.creepersan.bingimage.lib.json

import com.google.gson.Gson

object JsonUtils {
    private lateinit var mGson: Gson

    fun init(){
        mGson = Gson()
    }

    fun <T> parseJson(json: String, clazz: Class<T>): T?{
        return mGson.fromJson(json, clazz)
    }

    fun toString(obj: Any): String{
        return mGson.toJson(obj)
    }

}