package com.creepersan.bingimage.entrance

import com.creepersan.bingimage.config.ConfigUtils
import com.creepersan.bingimage.lib.database.bean.BingImageDatabaseBean
import com.creepersan.bingimage.lib.database.factory.DatabaseFactory
import com.creepersan.bingimage.lib.file.FileUtils
import com.creepersan.bingimage.lib.json.JsonUtils
import com.creepersan.bingimage.log.LogFactory
import com.creepersan.bingimage.utils.CompressUtils
import com.creepersan.bingimage.utils.TypeConvertUtils
import java.io.File
import java.sql.DriverManager
import java.sql.ResultSet
import java.util.*

class BackupNodejsBingImageApplication: BaseEntrance {
    companion object{
        const val TAG = "BackupNodejsBingImageApplication"
    }

    val scanner by lazy { Scanner(System.`in`) }

    override fun run(params: Array<String>) {
        println("正在从 Nodejs 版本 必应壁纸 中备份数据...")

        // 初始化
        JsonUtils.init()
        FileUtils.init()
        ConfigUtils.init()
        DatabaseFactory.init()

        // 从输入中获取数据
        print("请输入bing-image 数据库名称：")
        val nodeDatabaseName = scanner.nextLine()
        print("请输入bing-image MySQL数据库地址：")
        val nodeDatabaseAddress = scanner.nextLine()
        print("请输入bing-image MySQL数据库端口：")
        val nodeDatabasePort = scanner.nextLine()
        print("请输入bing-image MySQL数据库登录使用的用户名：")
        val nodeDatabaseUserName = scanner.nextLine()
        print("请输入bing-image MySQL数据库登录使用的密码：")
        val nodeDatabasePassword = scanner.nextLine()


        val backupInfoString = "backup_${TypeConvertUtils.timestampToBackupFilePostfix(System.currentTimeMillis())}"

        println("正在初始化备份文件夹...")
        val backupRootFolder: File
        try {
            backupRootFolder = File("${FileUtils.tmpFolder.absolutePath}/$backupInfoString")
            FileUtils.initFolder(backupRootFolder, "备份临时目录根目录")
        }catch (e: Exception){
            println("备份失败：${e.message}")
            return
        }

        // 备份文件夹的准备
        val backupDataFolder = File("${backupRootFolder.absolutePath}/data")
        FileUtils.initFolder(backupDataFolder, "备份临时目录数据目录")

        // 连接MySQL数据库的准备
        Class.forName("com.mysql.cj.jdbc.Driver")
        val connection = DriverManager.getConnection("jdbc:mysql://${nodeDatabaseAddress}:${nodeDatabasePort}/", nodeDatabaseUserName, nodeDatabasePassword);
        val statement = connection.createStatement()
        statement.execute("use $nodeDatabaseName")
        val resultSet = statement.executeQuery("select * from BingImage order by _id desc ")
        while (resultSet.next()){
            val bean = BingImageDatabaseBean(
                    _id = resultSet.getOrDefault("_id", 0),
                    year = resultSet.getOrDefault("year", 0),
                    month = resultSet.getOrDefault("month", 0),
                    day = resultSet.getOrDefault("day", 0),
                    copyright = resultSet.getOrDefault("copyright", ""),
                    path = resultSet.getOrDefault("path", ""),
                    title = resultSet.getOrDefault("title", ""),
                    location = resultSet.getOrDefault("location", ""),
                    author = resultSet.getOrDefault("author", ""),
                    json = resultSet.getOrDefault("json", "")
            )
            val json = JsonUtils.toString(bean)
            FileUtils.saveTextToFile("${backupDataFolder.absolutePath}/${bean._id}.json", json)
        }

        println("正在备份图片...")
        val backupBingImageFolder = File("${backupRootFolder.absolutePath}/bing_image")
        FileUtils.initFolder(backupBingImageFolder, "备份临时目录壁纸目录")
        println("由于兼容问题，暂不支持自动备份图片。请手动复制对应的图片文件夹至对应目录中")

        println("正在备份配置...")
        val backupConfigFolder = File("${backupRootFolder.absolutePath}/config")
        FileUtils.initFolder(backupConfigFolder, "备份临时目录配置目录")
        println("由于兼容问题，NodeJS版本配置文件暂不支持迁移，请重新进行配置")

        println("正在生成备份文件...")
        CompressUtils.zip(backupRootFolder, File("${FileUtils.backupFolder.absolutePath}/$backupInfoString.bak"))
    }

    inline fun <reified T> ResultSet.getOrDefault(key: String, default: T): T{
        return when(T::class){
            Int::class -> this.getInt(key) as? T ?: default
            String::class -> this.getString(key) as? T ?: default
            else -> {
                LogFactory.log(TAG, "从NodeJS必应壁纸的数据库中取出的数据类型匹配失败 key=$key")
                return default
            }
        }
    }

}
