package com.creepersan.bingimage.api.v1

import com.creepersan.bingimage.api.v1.bean.ImageInfoResponseBean
import com.creepersan.bingimage.config.ConfigUtils
import com.creepersan.bingimage.const.BingResolution
import com.creepersan.bingimage.lib.BeanConvert
import com.creepersan.bingimage.lib.database.factory.DatabaseFactory
import com.creepersan.bingimage.lib.file.FileUtils
import org.springframework.http.MediaType
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody
import java.awt.image.BufferedImage
import java.io.File
import java.io.FileInputStream
import java.lang.Exception
import java.util.ArrayList
import javax.imageio.ImageIO
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Controller
@RequestMapping("/")
class BingAPIController {


    /**
     * 获取最新的 1080P 的图片
     */
    @ResponseBody
    @RequestMapping("/api/v1/img-current")
    fun getCurrentImage(response: HttpServletResponse){
        val databaseOperator = DatabaseFactory.getDatabaseOperator(ConfigUtils.getBingImageConfig().databaseType)
        val list = databaseOperator.getBingImage(1, 0)
        if (list.isEmpty()){
            response.sendError(404, "Image not Found")
            return
        }
        val bean = list[0]

        val file = File("${FileUtils.bingImageFolder.absolutePath}/${bean.year}/${bean.month()}/${bean.day()}/1920x1080.jpg")

        if (!file.exists()){
            response.sendError(404, "Image not Found")
            return
        }

        responseImage(response, file)
    }

    /**
     * 获取指定范围的必应图片列表
     */
    @ResponseBody
    @RequestMapping("/api/v1/url")
    fun getImageListInfo(request: HttpServletRequest?): Map<String, Any>{
        val list = ArrayList<ImageInfoResponseBean>()
        val databaseOperator = DatabaseFactory.getDatabaseOperator(ConfigUtils.getBingImageConfig().databaseType)
        var page = request?.getParameter("page")?.toIntOrNull() ?: 0 // 页码
        if (page <= 0){
            page = 1
        }
        var count = request?.getParameter("count")?.toIntOrNull() ?: 12 // 每页读取的数量
        if (count <= 0){
            count = 1
        }
        if (count > 20){
            count = 20
        }
        databaseOperator.getBingImage(count, page).forEach { bean ->
            list.add(BeanConvert.bingImageDatabaseBean2ResponseBean(bean))
        }
        return V1Response.createSuccessResponse(mapOf<String, Any>(
                "itemCount" to databaseOperator.getBingImageCount(),
                "imgList" to list
        ))
    }

    /**
     * 获取指定数量的随机的必应图片
     */
    @ResponseBody
    @RequestMapping("/api/v1/random")
    fun getRandomImageListInfo(request: HttpServletRequest?): Map<String, Any>{
        val list = ArrayList<ImageInfoResponseBean>()
        val databaseOperator = DatabaseFactory.getDatabaseOperator(ConfigUtils.getBingImageConfig().databaseType)
        var count = request?.getParameter("count")?.toIntOrNull() ?: 1
        if (count < 1){
            count = 1
        }
        if (count > 20){
            count = 20
        }
        databaseOperator.getBingImageRandom(count).forEach { bean ->
            list.add(BeanConvert.bingImageDatabaseBean2ResponseBean(bean))
        }
        return V1Response.createSuccessResponse(list)
    }

    /**
     * 获取指定日期和分辨率的图片
     */
    @ResponseBody
    @RequestMapping("/api/v1/download/{date}.jpg")
    fun getImageInfo(@PathVariable date: String, request: HttpServletRequest, response: HttpServletResponse){
        if (date.length < 8){
            response.sendError(400, "data error")
            return
        }
        val year = date.substring(0, 4)
        val month = date.substring(4, 6)
        val day = date.substring(6, 8)

        val resolution : BingResolution
        try {
            resolution = BingResolution.getResolutionByID(request.getParameter("size").toInt())
        }catch (e:Exception){
            return
        }

        val imageFile = File("${FileUtils.bingImageFolder.absolutePath}/$year/$month/$day/${resolution.value}.jpg")

        if (!imageFile.exists()){
            response.sendError(404, "Image not Found")
            return
        }

        responseImage(response, imageFile)
    }

    /**
     * 获取对应的图片链接
     */
    @ResponseBody
    @RequestMapping(value = ["bing-image/{year}/{month}/{day}/{size}.jpg"], produces = arrayOf(MediaType.IMAGE_JPEG_VALUE))
    fun getImageFile(
            @PathVariable year: String,
            @PathVariable month: String,
            @PathVariable day: String,
            @PathVariable size: String,
            response: HttpServletResponse
    ) {

        val imageFile = File("${FileUtils.bingImageFolder.absolutePath}/$year/$month/$day/$size.jpg")

        if (!imageFile.exists()){
            response.sendError(404, "Image not Found")
            return
        }

        responseImage(response, imageFile)
    }


    /**
     * 返回图片
     * @param response 请求回调
     * @param file 图片文件
     */
    private fun responseImage(response: HttpServletResponse, file: File){
        response.contentType = "image/jpeg"
        val outStream = response.outputStream
        outStream.write(FileInputStream(file).readBytes())
        outStream.flush()
        outStream.close()

    }



}