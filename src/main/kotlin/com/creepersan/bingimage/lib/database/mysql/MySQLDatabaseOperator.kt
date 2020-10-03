package com.creepersan.bingimage.lib.database.mysql

import com.creepersan.bingimage.lib.database.bean.BingImageDatabaseBean
import com.creepersan.bingimage.lib.database.factory.DatabaseOperator

class MySQLDatabaseOperator : DatabaseOperator(){

    private fun getDatabaseConnection(){

    }

    override fun initTable() {

    }


    override fun insertOrUpdateBingImage(bingImage: BingImageDatabaseBean) {
        TODO("Not yet implemented")
    }

    override fun removeBingImage(id: Int) {
        TODO("Not yet implemented")
    }

    override fun getBingImage(count: Int, index: Int): List<BingImageDatabaseBean> {
        TODO("Not yet implemented")
    }

    override fun getBingImageCount(): Int {
        TODO("Not yet implemented")
    }

    override fun getBingImageRandom(count: Int): List<BingImageDatabaseBean> {
        TODO("Not yet implemented")
    }

}