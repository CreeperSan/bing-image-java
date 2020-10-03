package com.creepersan.bingimage.entrance

import com.creepersan.bingimage.config.ConfigUtils
import com.creepersan.bingimage.config.type.BingImageConfigDatabaseType
import com.creepersan.bingimage.lib.database.factory.DatabaseFactory
import com.creepersan.bingimage.lib.file.FileUtils
import com.creepersan.bingimage.lib.json.JsonUtils
import com.creepersan.bingimage.utils.CompressUtils
import com.creepersan.bingimage.utils.TypeConvertUtils
import java.io.File
import java.util.zip.ZipOutputStream

class BackupEntrance : BaseEntrance{
    var isInterrupt = false

    override fun run(params: Array<String>) {
        isInterrupt = false
        val backupInfoString = "backup_${TypeConvertUtils.timestampToBackupFilePostfix(System.currentTimeMillis())}"


        println("正在生成备份...")
        try {
            JsonUtils.init()
            FileUtils.init()
        } catch (e: Exception) {
            println("备份失败：${e.message}")
            return
        }


        println("正在初始化备份文件夹...")
        val backupRootFolder: File
        try {
            backupRootFolder = File("${FileUtils.tmpFolder.absolutePath}/$backupInfoString")
            FileUtils.initFolder(backupRootFolder, "备份临时目录根目录")
        }catch (e: Exception){
            println("备份失败：${e.message}")
            return
        }


        try {

            println("正在备份数据库数据库...")
            val backupDataFolder = File("${backupRootFolder.absolutePath}/data")
            FileUtils.initFolder(backupDataFolder, "备份临时目录数据目录")
            val bingConfig = ConfigUtils.getBingImageConfig()
            when(bingConfig.databaseType){
                BingImageConfigDatabaseType.MySQL,
                BingImageConfigDatabaseType.SQLite-> {
                    val sqlConfig = ConfigUtils.getSQLiteConfig()
                    val databaseOperator = DatabaseFactory.getDatabaseOperator(bingConfig.databaseType)
                    val count = databaseOperator.getBingImageCount()
                    for (i in 0 until count){
                        val bingImageDatabaseBeanList = databaseOperator.getBingImage(1, i)
                        if (bingImageDatabaseBeanList.isEmpty()){
                            continue
                        }
                        val bingImageDatabaseBean = bingImageDatabaseBeanList[0]
                        println("正在提取 ${bingImageDatabaseBean.year}年${bingImageDatabaseBean.month()}月${bingImageDatabaseBean.day()}日 的数据库数据【${i+1}/$count】")
                        val json = JsonUtils.toString(bingImageDatabaseBean)
                        FileUtils.saveTextToFile("${backupDataFolder.absolutePath}/${bingImageDatabaseBean._id}.json", json)
                    }
                }
                else -> {
                    println("尚未支持此类数据库的备份方式")
                    return
                }
            }


            println("正在备份图片...")
            val backupBingImageFolder = File("${backupRootFolder.absolutePath}/bing_image")
            FileUtils.initFolder(backupBingImageFolder, "备份临时目录壁纸目录")
            copyFile(FileUtils.bingImageFolder, backupBingImageFolder){ src, _ ->
                println("正在提取 $src")
            }


            println("正在备份配置...")
            val backupConfigFolder = File("${backupRootFolder.absolutePath}/config")
            FileUtils.initFolder(backupConfigFolder, "备份临时目录配置目录")
            copyFile(FileUtils.configFolder, backupConfigFolder){ src, _ ->
                println("正在提取 $src")
            }


            println("正在生成备份文件...")
            CompressUtils.zip(backupRootFolder, File("${FileUtils.backupFolder.absolutePath}/$backupInfoString.bak"))


            println("备份完成！")

        } catch (e: Exception) {
            isInterrupt = true
            println("发生错误：${e.message}")
        } finally {
            println("正在清理缓存...")
            FileUtils.deleteFile(backupRootFolder)

            println("备份${if (isInterrupt) "失败" else "成功，备份文件已保存于 ${FileUtils.backupFolder.absolutePath}/$backupInfoString.bak"}")
        }
    }

    /**
     * 递归复制文件
     */
    private fun copyFile(srcFile: File, dstFile: File, action: ((src:File, dst:File)->Unit)? = null){
        action?.invoke(srcFile, dstFile)
        // 文件不存在或者目标文件存在
        if(!srcFile.exists()){
            throw Exception("源文件不存在")
        }
        if (dstFile.exists()){
            if (srcFile.isFile && !dstFile.isFile){
                throw Exception("目标文件已经存在，且源文件不是文件")
            }
            if (srcFile.isDirectory && !dstFile.isDirectory){
                throw Exception("目标文件夹已经存在，且源文件不是文件夹")
            }
        }
        // 复制逻辑
        if (srcFile.isFile){
            // 是文件
            // 如果文件不存在，则创建文件
            if(!dstFile.exists()){
                if (!dstFile.createNewFile()){
                    throw Exception("创建文件失败 ${dstFile.absolutePath}")
                }
            }
            // 复制文件
            srcFile.copyTo(dstFile, true)
        } else {
            // 是目录
            dstFile.mkdirs()
            srcFile.listFiles()?.forEach {
                val fromFile = File(srcFile.absolutePath, it.name)
                val toFile = File(dstFile.absolutePath, it.name)
                copyFile(fromFile, toFile, action)
            }
        }
    }

}