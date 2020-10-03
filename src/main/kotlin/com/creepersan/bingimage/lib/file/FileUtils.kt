package com.creepersan.bingimage.lib.file

import com.creepersan.bingimage.const.BingResolution
import com.creepersan.bingimage.lib.file.exception.FilePathException
import com.creepersan.bingimage.log.LogFactory
import com.creepersan.bingimage.log.Logger
import com.creepersan.bingimage.utils.TypeConvertUtils
import java.io.File
import java.io.FileOutputStream

object FileUtils {
    const val TAG = "FileUtils"

    val resourcesRootFolder = File("resources")
    val bingImageFolder = File("${resourcesRootFolder.absolutePath}/bing_image")
    val logcatFolder = File("${resourcesRootFolder.absolutePath}/logcat")
    val backupFolder = File("${resourcesRootFolder.absolutePath}/backup")
    val sqliteFolder = File("${resourcesRootFolder.absolutePath}/sqlite")
    val configFolder = File("${resourcesRootFolder.absolutePath}/config")
    val tmpFolder = File("${resourcesRootFolder.absolutePath}/tmp")

    /**
     * 初始化基础的文件夹
     */
    fun init(){
        initFolder(resourcesRootFolder, "存放所有资源")
        initFolder(resourcesRootFolder, "存放所有资源")
        initFolder(bingImageFolder, "存放必应图片")
        initFolder(logcatFolder, "存放日志输出")
        initFolder(backupFolder, "存放备份")
        initFolder(sqliteFolder, "存放SqlLite数据库")
        initFolder(configFolder, "存放应用配置")
        initFolder(tmpFolder, "存放临时数据")
    }

    /**
     * 初始化特定功能的文件夹
     * @param folder 文件夹
     * @param folderDescription 文件夹描述，仅用于日志
     */
    fun initFolder(folder: File, folderDescription: String){
        if (folder.exists() && folder.isFile){
            val msg = "${folderDescription}的文件夹对应的路径为一个文件！${folder.absolutePath}"
            LogFactory.log(TAG, msg, Logger.Level.Error)
            throw FilePathException(msg)
        }
        if (!folder.exists()){
            val result = folder.mkdirs()
            if (!result){
                val msg = "创建${folderDescription}的文件夹失败"
                LogFactory.log(TAG, msg, Logger.Level.Error)
                throw FilePathException(msg)
            }
        }
    }

    /**
     * 将文本写入文件
     * @param fileName 文件名称
     * @param content 要写入的文件内容
     */
    fun saveTextToFile(filePath: String, content: String): Boolean{
        val file = File(filePath)
        if (!file.exists()){
            if (!file.createNewFile()){
                return false
            }
        }
        file.writeText(content)
        return true
    }

    /**
     * 从文本读取文本内容
     * @param filePath 文件路径
     * @return 文本内容，如果文件不存在，则返回 null
     */
    fun loadTextFromFile(filePath: String): String?{
        val file = File(filePath)
        if (!file.exists()){
            return null
        }
        return try {
            file.readText()
        } catch (e: Exception) {
            LogFactory.log(TAG, "读取文件内容失败 message=${e.message} path=$filePath", Logger.Level.Warming)
            null
        }
    }

    /**
     * 保存图片
     * @param byteArray 图片二进制数据
     * @param year 年份
     * @param month 月份
     * @param day 日期
     * @param resolution 图片的分辨率
     */
    fun saveImage(byteArray: ByteArray, year: Int, month: Int, day: Int, resolution: BingResolution): File{
        val yearStr = year.toString()
        val monthStr = TypeConvertUtils.intTo2LengthString(month)
        val dayStr = TypeConvertUtils.intTo2LengthString(day)
        val imageSaveFolder = File("${bingImageFolder.absolutePath}/$yearStr/$monthStr/$dayStr")

        initFolder(imageSaveFolder, "存放日期为 $yearStr-$monthStr-$dayStr 分辨率为 ${resolution.value} 必应背景图片")

        val imageFile = File("${imageSaveFolder.absolutePath}/${resolution.value}.jpg")
        if (!imageFile.exists() && !imageFile.createNewFile()){
            val msg = "创建图片文件${imageFile.absolutePath}失败"
            LogFactory.log(TAG, msg, Logger.Level.Error)
            throw FilePathException(msg)
        }

        val fos = FileOutputStream(imageFile)
        fos.write(byteArray)
        fos.flush()
        fos.close()

        return imageFile
    }

    /**
     * 递归删除文件
     * @param file 要删除的文件
     */
    fun deleteFile(file: File){
        if(!file.exists()){
            return
        }
        if (file.isFile){
            file.delete()
        } else {
            file.listFiles()?.forEach {
                deleteFile(it)
            }
            file.delete()
        }
    }

    /**
     * 递归复制文件
     * @param srcFile 源文件
     * @param dstFile 目标文件
     */
    fun copyFile(srcFile: File, dstFile: File){
        if (!srcFile.exists()){
            return
        }
//        println("copy -> ${srcFile.absolutePath}    to -> ${dstFile.absolutePath}")
        if(dstFile.exists()){
            dstFile.delete()
        }
        if (srcFile.isFile){
            srcFile.copyTo(dstFile, true)
        } else {
            srcFile.copyTo(dstFile, true)
            val listFiles = srcFile.listFiles() ?: return
            for (tmpFile in listFiles){
                copyFile(tmpFile, File("${dstFile.absolutePath}/${tmpFile.name}"))
            }
        }
    }

}