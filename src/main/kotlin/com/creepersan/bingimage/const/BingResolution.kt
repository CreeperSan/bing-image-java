package com.creepersan.bingimage.const

import java.lang.Exception

enum class BingResolution(val value: String) {
    L_1920_1200 ("1920x1200"),
    L_1920_1080 ("1920x1080"),
    L_1366_768  ("1366x768"),
    L_1280_720  ("1280x720"),
    L_1024_768  ("1024x768"),
    L_800_600   ("800x600"),
    L_800_480   ("800x480"),
    L_640_480   ("640x480"),
    L_400_240   ("400x240"),
    L_320_240   ("320x240"),
    P_1080_1920 ("1080x1920"),
    P_768_1366  ("768x1366"),
    P_768_1280  ("768x1280"),
    P_720_1280  ("720x1280"),
    P_480_800   ("480x800"),
    P_480_640   ("480x640"),
    P_240_400   ("240x400"),
    P_240_320   ("240x320");

    companion object{
        fun allResolution(): List<BingResolution> = listOf(
                L_1920_1200,
                L_1920_1080,
                L_1366_768,
                L_1280_720,
                L_1024_768,
                L_800_600,
                L_800_480,
                L_640_480,
                L_400_240,
                L_320_240,
                P_1080_1920,
                P_768_1366,
                P_768_1280,
                P_720_1280,
                P_480_800,
                P_480_640,
                P_240_400,
                P_240_320
        )

        fun getResolutionByID(resolution: Int): BingResolution{
            return when(resolution){
                0 -> L_1920_1200
                1 -> L_1920_1080
                2 -> L_1366_768
                3 -> L_1280_720
                4 -> L_1024_768
                5 -> L_800_600
                6 -> L_800_480
                7 -> L_640_480
                8 -> L_400_240
                9 -> L_320_240
                10 -> P_1080_1920
                11 -> P_768_1366
                12 -> P_768_1280
                13 -> P_720_1280
                14 -> P_480_800
                15 -> P_480_640
                16 -> P_240_400
                17 -> P_240_320
                else -> throw Exception("Unknown Resolution=$resolution")
            }
        }
    }
}

val RESOLUTION_ARRAY = arrayOf(
        BingResolution.L_1920_1200,
        BingResolution.L_1920_1080,
        BingResolution.L_1366_768,
        BingResolution.L_1280_720,
        BingResolution.L_1024_768,
        BingResolution.L_800_600,
        BingResolution.L_800_480,
        BingResolution.L_640_480,
        BingResolution.L_400_240,
        BingResolution.L_320_240,
        BingResolution.P_1080_1920,
        BingResolution.P_768_1366,
        BingResolution.P_768_1280,
        BingResolution.P_720_1280,
        BingResolution.P_480_800,
        BingResolution.P_480_640,
        BingResolution.P_240_400,
        BingResolution.P_240_320
)
