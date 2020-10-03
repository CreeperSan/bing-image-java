package com.creepersan.bingimage.entrance

import com.creepersan.bingimage.config.ConfigUtils
import com.creepersan.bingimage.config.type.BingImageConfigDatabaseType
import com.creepersan.bingimage.config.type.BingImageConfigLoadCount
import com.creepersan.bingimage.config.type.BingImageConfigLocation
import com.creepersan.bingimage.lib.file.FileUtils
import com.creepersan.bingimage.lib.json.JsonUtils
import java.util.*

class ConfigEntrance: BaseEntrance {
    private val scanner by lazy { Scanner(System.`in`) }

    override fun run(params: Array<String>) {
        println("正在初始化...")
        JsonUtils.init()
        FileUtils.init()
        ConfigUtils.init()

        println("")
        println("欢迎来到配置中心")
        println("请根据控制台提示进行对应配置")

        mainEntrance()
    }

    /**
     * 主菜单
     */
    private fun mainEntrance(){
        println("")
        println("请选择要编辑的配置")
        println("1 - 应用配置")
        println("2 - MySQL数据库配置")
        println("3 - SQLite数据库配置")
        println("其他 - 退出编辑")
        print("请输入要编辑的配置：")
        when(scanner.nextLine()){
            "1" -> {
                bingImageEntrance()
            }
            "2" -> {
                mySqlEntrance()
            }
            "3" -> {
                sqLiteEntrance()
            }
            else -> {
                println("感谢使用")
            }
        }
    }

    /**
     * 应用配置菜单
     */
    private fun bingImageEntrance(){
        println("")
        println("正在进行应用配置")

        val config = ConfigUtils.getBingImageConfig()

        println("请输入应用地区（根据服务器所在地区进行设置）")
        println("1 - 中国大陆")
        println("2 - 日本")
        println("3 - 英国")
        println("其他 - 不修改此选项")
        print("请输入应用地区（当前设置为${config.bingImageLocation.toFormattedString()}）：")
        config.bingImageLocation = when(scanner.nextLine()){
            "1" -> BingImageConfigLocation.ChinaMainland
            "2" -> BingImageConfigLocation.Japan
            "3" -> BingImageConfigLocation.England
            else -> config.bingImageLocation
        }

        println("请输入储存必应壁纸的数据库")
        println("1 - MySQL")
        println("2 - SQLite")
        println("其他 - 不修改此配置")
        print("请输入要使用的数据库类型（当前设置为${config.databaseType}）：")
        config.databaseType = when(scanner.nextLine()){
            "1" -> BingImageConfigDatabaseType.MySQL
            "2" -> BingImageConfigDatabaseType.SQLite
            else -> config.databaseType
        }

        println("请输入每次同步壁纸时同步的数量")
        println("支持 1 - 14 之间的整数，输入其他为不修改此配置")
        print("请输入每次同步壁纸时的同步数量（当前设置为${config.bingImageLoadCount}）：")
        config.bingImageLoadCount = when(scanner.nextLine()){
            "1" -> BingImageConfigLoadCount.One
            "2" -> BingImageConfigLoadCount.Two
            "3" -> BingImageConfigLoadCount.Three
            "4" -> BingImageConfigLoadCount.Four
            "5" -> BingImageConfigLoadCount.Five
            "6" -> BingImageConfigLoadCount.Six
            "7" -> BingImageConfigLoadCount.Seven
            "8" -> BingImageConfigLoadCount.Eight
            "9" -> BingImageConfigLoadCount.Nine
            "10" -> BingImageConfigLoadCount.Ten
            "11" -> BingImageConfigLoadCount.Eleven
            "12" -> BingImageConfigLoadCount.Twelve
            "13" -> BingImageConfigLoadCount.Thirteen
            "14" -> BingImageConfigLoadCount.Fourteen
            else -> config.bingImageLoadCount
        }

        println("正在写入配置...")
        ConfigUtils.writeBingImageConfig(config)

        println("应用配置完成")
        println("正在返回配置中心")
        mainEntrance()
    }

    /**
     * MySQL数据库配置菜单
     */
    private fun mySqlEntrance(){
        println("")
        println("正在进行MySQL数据库配置")

        var input = ""

        println("请根据提示输入对应信息（为空则不修改对应配置）")
        val config = ConfigUtils.getMySQLConnectionConfig()

        print("请输入数据库地址(当前 ${config.hostAddress})：")
        input = scanner.nextLine()
        if (input.isNotEmpty()){
            config.hostAddress = input
        }

        print("请输入数据库名称(当前 ${config.databaseName})：")
        input = scanner.nextLine()
        if (input.isNotEmpty()){
            config.databaseName = input
        }

        print("请输入数据库用户名(当前 ${config.databaseUsername})：")
        input = scanner.nextLine()
        if (input.isNotEmpty()){
            config.databaseUsername = input
        }

        print("请输入数据库密码：")
        input = scanner.nextLine()
        if (input.isNotEmpty()){
            config.databasePassword = input
        }

        println("正在写入配置...")
        ConfigUtils.writeMySQLConnectionConfig(config)

        println("MySQL数据库配置完成")
        println("正在返回配置中心")
        mainEntrance()
    }

    /**
     * SQLite数据库配置菜单
     */
    private fun sqLiteEntrance(){
        println("")
        println("正在进行SQLite数据库配置")

        val config = ConfigUtils.getSQLiteConfig()
        println("暂无配置项可以配置")

        println("SQLite数据库配置完成")
        println("正在返回配置中心")
        mainEntrance()
    }

}