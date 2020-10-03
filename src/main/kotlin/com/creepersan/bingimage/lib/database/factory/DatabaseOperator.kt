package com.creepersan.bingimage.lib.database.factory

import com.creepersan.bingimage.lib.database.bean.BingImageDatabaseBean

abstract class DatabaseOperator {

    abstract fun initTable()

    abstract fun insertOrUpdateBingImage(bingImage: BingImageDatabaseBean)

    abstract fun removeBingImage(id: Int)

    fun removeBingImage(bingImage: BingImageDatabaseBean) = removeBingImage(bingImage._id)

    abstract fun getBingImage(count: Int, index: Int): List<BingImageDatabaseBean>

    abstract fun getBingImageRandom(count: Int): List<BingImageDatabaseBean>

    abstract fun getBingImageCount(): Int

}