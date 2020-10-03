package com.creepersan.bingimage.entrance

import com.creepersan.bingimage.config.ConfigUtils
import com.creepersan.bingimage.lib.database.bean.BingImageDatabaseBean
import com.creepersan.bingimage.lib.database.factory.DatabaseFactory
import com.creepersan.bingimage.lib.file.FileUtils
import com.creepersan.bingimage.lib.json.JsonUtils
import com.creepersan.bingimage.utils.CompressUtils
import java.io.File
import java.util.*

class RestoreEntrance: BaseEntrance {

    override fun run(params: Array<String>) {
        if (params.size <= 1){
            showUsageHint()
            return
        }

        val backupFile = File(params[1])

        if (!backupFile.exists() || backupFile.isDirectory){
            println("备份文件不存在 path=${backupFile.absolutePath}")
            return
        }

        // 基础组件初始话
        println("正在初始化环境")
        JsonUtils.init()
        FileUtils.init()

        // 初始化缓存目录
        val tmpRootFolder = File("${FileUtils.tmpFolder.absolutePath}/restore_${System.currentTimeMillis()}")
        FileUtils.initFolder(tmpRootFolder, "临时备份文件解包目录")

        // 解包备份文件
        println("正在解包备份文件")
        CompressUtils.unzip(backupFile, tmpRootFolder)
        println("备份文件解包完成")

        // 出错校验
        if (!tmpRootFolder.exists()){
            println("备份文件解包失败，找不到解包后的文件夹")
            return
        }
        if (tmpRootFolder.exists() && tmpRootFolder.isFile){
            println("备份文件解包失败，也许这并不是备份文件，你可以尝试手动恢复数据")
            return
        }
        val tmpRootFolderListFile = tmpRootFolder.listFiles()
        if (null == tmpRootFolderListFile || tmpRootFolderListFile.size != 1){
            println("备份文件解包失败，解包后没有发现备份内容，也许这并不是备份文件，你可以尝试手动恢复数据")
            return
        }

        // 检查文件是否都存在
        val tmpExtraRootFolder = tmpRootFolderListFile[0]
        val bingImageRestoreFolder = File("${tmpExtraRootFolder.absolutePath}/bing_image")
        if (!bingImageRestoreFolder.exists() || bingImageRestoreFolder.isFile){
            println("备份文件解包失败，解包后没有发现备份的必应图片文件，也许这并不是备份文件，你可以尝试手动恢复数据")
        }
        val configRestoreFolder = File("${tmpExtraRootFolder.absolutePath}/config")
        if (!configRestoreFolder.exists() || configRestoreFolder.isFile){
            println("备份文件解包失败，解包后没有发现备份的配置文件，也许这并不是备份文件，你可以尝试手动恢复数据")
        }
        val dataRestoreFolder = File("${tmpExtraRootFolder.absolutePath}/data")
        if (!dataRestoreFolder.exists() || dataRestoreFolder.isFile){
            println("备份文件解包失败，解包后没有发现备份的数据文件，也许这并不是备份文件，你可以尝试手动恢复数据")
        }

        // 恢复图片文件
        println("正在恢复必应图片")
        FileUtils.copyFile(bingImageRestoreFolder, FileUtils.bingImageFolder)
        println("必应图片恢复完毕")

        // 恢复配置文件
        println("正在恢复配置文件")
        FileUtils.copyFile(configRestoreFolder, FileUtils.configFolder)
        println("配置文件恢复完毕")

        // 恢复数据库
        val dataFileList = dataRestoreFolder.listFiles()
        if (null == dataFileList || dataFileList.isEmpty()){
            println("暂未发现必应数据")
        } else {
            val bingConfig =  ConfigUtils.getBingImageConfig()
            val databaseOperator = DatabaseFactory.getDatabaseOperator(bingConfig.databaseType)
            databaseOperator.initTable()
            for (dataFile in dataFileList){
                println("正在恢复 ${dataFile.nameWithoutExtension}")
                val dataFileContent = dataFile.readText()
                val bean = JsonUtils.parseJson(dataFileContent, BingImageDatabaseBean::class.java)
                if (null == bean){
                    println("数据 ${dataFile.nameWithoutExtension} 恢复失败")
                    continue
                }
                databaseOperator.insertOrUpdateBingImage(bean)
            }
        }
        println("恢复数据完毕")

        // 正在清理缓存
        println("正在清理缓存...")
        FileUtils.deleteFile(tmpRootFolder)
        println("缓存清理完成")
    }

    private fun showUsageHint(){
        println("参数错误")
        println("用法:")
        println("    backup [备份文件路径]")
    }


}