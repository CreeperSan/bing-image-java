package com.creepersan.bingimage.config

import com.creepersan.bingimage.config.bean.BaseConfig
import com.creepersan.bingimage.config.bean.BingImageGlobalConfig
import com.creepersan.bingimage.config.bean.MySQLConnectionConfig
import com.creepersan.bingimage.config.bean.SQLiteConnectionConfig
import com.creepersan.bingimage.config.type.BingImageConfigDatabaseType
import com.creepersan.bingimage.lib.file.FileUtils
import com.creepersan.bingimage.lib.json.JsonUtils
import com.creepersan.bingimage.log.LogFactory
import com.creepersan.bingimage.log.Logger
import java.io.File
import java.io.FileOutputStream
import java.lang.Exception

object ConfigUtils {
    private const val TAG : String = "ConfigUtils"

    private val mCache = HashMap<String, BaseConfig>()

    /**
     * get config file
     * @param fileName config file name, with postfix
     * @return config file
     */
    private fun getConfigFile(fileName: String): File{
        // make sure the file exist
        val configFolder = FileUtils.configFolder
        val bingImageConfigFile = File("${configFolder.absolutePath}/$fileName") // BingImage config file
        if (!bingImageConfigFile.exists()){ // if file not exist
            if (!bingImageConfigFile.createNewFile()){ // then create it
                // if can not create,then throw exception
                throw Exception("Exception occurred when initializing BingImage config files! Could not create config file.")
            } else {
                bingImageConfigFile.writeText("{}")
            }
        } else { // config file already exist
            if (!bingImageConfigFile.isFile){ // but if the path is not a file, then throw exception
                throw Exception("Exception occurred when initializing BingImage config files! the path=${configFolder.absolutePath} was not a file!")
            }
        }
        return bingImageConfigFile
    }

    @Synchronized
    private inline fun <reified T:BaseConfig> getConfig(fileName: String): T{
        if (!mCache.containsKey(fileName)){
            val bingImageConfigFile = getConfigFile(fileName)

            // read file content
            var fileContent = ""
            try {
                fileContent = bingImageConfigFile.reader().readText()
            } catch (e: Exception) {
                throw Exception("Exception occurred when reading BingImage config files! message=${e.message}")
            }

            // convert json string to model
            try {
                val tmpConfig = JsonUtils.parseJson(fileContent, T::class.java) ?: throw Exception("config bean is null")
                mCache[fileName] = tmpConfig
                return tmpConfig
            } catch (e: Exception) {
                throw Exception("Exception occurred when converting config files to config bean! message=${e.message}")
            }
        }

        return mCache[fileName] as T

    }

    @Synchronized
    private fun writeConfig(fileName: String, config: BaseConfig){
        var configJson = ""

        try {
            configJson = JsonUtils.toString(config)
        } catch (e: Exception) {
            throw Exception("Exception occurred when converting config bean to jsonString! message=${e.message}")
        }

        val configFile = getConfigFile(fileName)

        // update config in storage
        try {
            val fos = FileOutputStream(configFile)
            fos.write(configJson.toByteArray())
            fos.flush()
            fos.close()
        } catch (e: Exception) {
            throw Exception("Exception occurred when writing config string to file! message=${e.message}")
        }

        mCache[fileName] = config

    }

    fun getBingImageConfig(): BingImageGlobalConfig{
        return getConfig(BaseConfig.BING_IMAGE)
    }

    fun writeBingImageConfig(config: BingImageGlobalConfig){
        writeConfig(BaseConfig.BING_IMAGE, config)
    }

    fun getMySQLConnectionConfig(): MySQLConnectionConfig{
        return getConfig(BaseConfig.MYSQL_CONNECTION)
    }

    fun writeMySQLConnectionConfig(config: MySQLConnectionConfig){
        writeConfig(BaseConfig.MYSQL_CONNECTION, config)
    }

    fun getSQLiteConfig(): SQLiteConnectionConfig{
        return getConfig(BaseConfig.SQLITE_CONNECTION)
    }

    fun writeSQLiteConfig(config: SQLiteConnectionConfig){
        writeConfig(BaseConfig.SQLITE_CONNECTION, config)
    }

    fun init(){
        LogFactory.log(TAG, "正在初始化")
        LogFactory.log(TAG, "正在读取应用配置")
        val bingImageConfig = getBingImageConfig()
        LogFactory.log(TAG, "读取应用配置成功")
        LogFactory.log(TAG, "所在地区：${bingImageConfig.bingImageLocation.toFormattedString()}")
        LogFactory.log(TAG, "每次拉取数量：${bingImageConfig.bingImageLoadCount}")
        LogFactory.log(TAG, "数据库类型：${bingImageConfig.databaseType}")
        when(bingImageConfig.databaseType){
            BingImageConfigDatabaseType.SQLite -> {
                LogFactory.log(TAG, "正在加载SQLite数据库配置")
                val sqlConfig = getSQLiteConfig()
                LogFactory.log(TAG, "读取数据库配置成功")
            }
            BingImageConfigDatabaseType.MySQL -> {
                LogFactory.log(TAG, "正在加载MySQL数据库配置")
                LogFactory.log(TAG, "暂未支持MySQL数据库，正在退出")
                System.exit(0)
            }
            else -> {
                LogFactory.log(TAG, "尚未支持的数据库类型，正在退出")
                System.exit(0)
            }
        }
        LogFactory.log(TAG, "初始化完成")
    }


}