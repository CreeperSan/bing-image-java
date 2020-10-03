package com.creepersan.bingimage.lib.database.factory

import com.creepersan.bingimage.config.ConfigUtils
import com.creepersan.bingimage.config.type.BingImageConfigDatabaseType
import com.creepersan.bingimage.lib.database.mysql.MySQLDatabaseOperator
import com.creepersan.bingimage.lib.database.sqlite.SQLiteDatabaseOperator
import com.creepersan.bingimage.log.LogFactory
import com.sun.javafx.logging.Logger

object DatabaseFactory {
    private const val TAG = "DatabaseFactory"

    private lateinit var sqLiteDatabaseOperator: DatabaseOperator
    private lateinit var mySQLDatabaseOperator: DatabaseOperator

    private lateinit var mDefaultDatabaseOperator: DatabaseOperator

    fun getDatabaseOperator(databaseType: BingImageConfigDatabaseType): DatabaseOperator{
        return when(databaseType){
            BingImageConfigDatabaseType.SQLite -> getSQLiteDatabaseOperator()
            BingImageConfigDatabaseType.MySQL -> getMySQLDatabaseOperator()
        }
    }

    private fun getSQLiteDatabaseOperator(): DatabaseOperator{
        if (!this::sqLiteDatabaseOperator.isInitialized){
            sqLiteDatabaseOperator = SQLiteDatabaseOperator()
        }
        return sqLiteDatabaseOperator
    }

    private fun getMySQLDatabaseOperator(): DatabaseOperator{
        if (!this::mySQLDatabaseOperator.isInitialized){
            mySQLDatabaseOperator = MySQLDatabaseOperator()
        }
        return mySQLDatabaseOperator
    }

    private fun getDefaultDatabaseOperator(): DatabaseOperator{
        if (!this::mDefaultDatabaseOperator.isInitialized){
            LogFactory.log(TAG, "数据库链接尚为初始化")
            System.exit(0)
        }
        return mDefaultDatabaseOperator
    }

    /**
     * 初始化数据库
     * 需要注意的是，在此之前需要先初始化配置信息
     */
    fun init(){
        LogFactory.log(TAG, "正在初始化数据库")
        val bingConfig = ConfigUtils.getBingImageConfig()
        when(bingConfig.databaseType){
            BingImageConfigDatabaseType.SQLite -> {
                LogFactory.log(TAG, "正在初始化SQLite数据库表")
                mDefaultDatabaseOperator = getSQLiteDatabaseOperator()
                mDefaultDatabaseOperator.initTable()
            }
            BingImageConfigDatabaseType.MySQL -> {
                LogFactory.log(TAG, "正在初始化MySQL数据库表")
                mDefaultDatabaseOperator = getMySQLDatabaseOperator()
                mDefaultDatabaseOperator.initTable()
            }
            else -> {
                LogFactory.log(TAG, "未知的数据库类型")
            }
        }
        LogFactory.log(TAG, "初始化数据库表完成")
    }

}