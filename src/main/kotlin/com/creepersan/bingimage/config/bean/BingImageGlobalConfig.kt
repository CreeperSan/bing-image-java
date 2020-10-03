package com.creepersan.bingimage.config.bean

import com.creepersan.bingimage.config.type.BingImageConfigDatabaseType
import com.creepersan.bingimage.config.type.BingImageConfigLoadCount
import com.creepersan.bingimage.config.type.BingImageConfigLocation

data class BingImageGlobalConfig(
        var databaseType: BingImageConfigDatabaseType = BingImageConfigDatabaseType.SQLite,
        var bingImageLocation: BingImageConfigLocation = BingImageConfigLocation.ChinaMainland,
        var bingImageLoadCount: BingImageConfigLoadCount = BingImageConfigLoadCount.Seven
) : BaseConfig()