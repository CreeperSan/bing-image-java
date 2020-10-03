package com.creepersan.bingimage

import com.creepersan.bingimage.entrance.*
import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication
class BingImageApplication

fun main(args: Array<String>) {
    // 解析启动参数
    when{
        args.isEmpty() -> { // 启动应用
            MainEntrance().run(args)
        }
        args.isNotEmpty() && args[0] == "help" -> { // 帮助
            HelpEntrance().run(args)
        }
        args.isNotEmpty() && args[0] == "config" -> { // 配置
            ConfigEntrance().run(args)
        }
        args.isNotEmpty() && args[0] == "backup" -> { // 备份
            BackupEntrance().run(args)
        }
        args.isNotEmpty() && args[0] == "restore" -> { // 恢复
            RestoreEntrance().run(args)
        }
        else -> { // 默认为帮助
            HelpEntrance().run(args)
        }
    }

}
