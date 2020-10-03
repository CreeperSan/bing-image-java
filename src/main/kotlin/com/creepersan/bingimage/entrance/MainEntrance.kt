package com.creepersan.bingimage.entrance

import com.creepersan.bingimage.BingImageApplication
import com.creepersan.bingimage.config.ConfigUtils
import com.creepersan.bingimage.lib.database.factory.DatabaseFactory
import com.creepersan.bingimage.lib.file.FileUtils
import com.creepersan.bingimage.lib.json.JsonUtils
import com.creepersan.bingimage.lib.network.NetworkManager
import com.creepersan.bingimage.task.FetchBingImageFromServerTask
import org.springframework.boot.runApplication

class MainEntrance: BaseEntrance {

    override fun run(params: Array<String>) {
        // 初始化库
        FileUtils.init()                // 初始化文件夹
        JsonUtils.init()                // 初始化JSON解析器
        NetworkManager.init()           // 初始化网络组件
        ConfigUtils.init()              // 初始化应用配置
        DatabaseFactory.init()          // 初始化数据库
        // 应用启动时自动执行的认为
        FetchBingImageFromServerTask().fetchBingImageData()    // 自动拉取必应图片
        // 运行应用
        runApplication<BingImageApplication>(*params)
    }

}