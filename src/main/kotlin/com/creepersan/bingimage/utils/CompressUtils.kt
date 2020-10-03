package com.creepersan.bingimage.utils

import java.io.File
import java.io.FileOutputStream
import java.util.zip.ZipEntry
import java.util.zip.ZipFile
import java.util.zip.ZipOutputStream

object CompressUtils {


    /**
     * 解压zip压缩文件文件
     * @param src 要解压的压缩文件（必须要存在）
     */
    fun unzip(src: File, dst: File){
        if(!src.exists()){
            throw Exception("源文件不存在 ${src.absolutePath}")
        }
        if (dst.exists() && dst.isFile){
            throw Exception("目标位置为文件 ${dst.absolutePath}")
        }
        if (!dst.exists() && !dst.mkdirs()){
            throw Exception("创建目标文件夹失败 ${dst.absolutePath}")
        }
        val zipFile = ZipFile(src)
        val zipEnumeration = zipFile.entries()
        while (zipEnumeration.hasMoreElements()){
            val zipEntry = zipEnumeration.nextElement()
            val zipEntryName = zipEntry.name
            val zipInputStream = zipFile.getInputStream(zipEntry)
            val outPath = "${dst.absolutePath}/$zipEntryName".replace("\\*", "/")
            println(zipEntryName)
            val outFile = File(outPath.substring(0, outPath.lastIndexOf("/")))
            if (!outFile.exists()){
                outFile.mkdirs()
            }
            if (File(outPath).isDirectory){
                continue
            }
            File(outPath).writeBytes(zipInputStream.readBytes())
            zipInputStream.close()
        }
    }

    /**
     * 压缩一系列文件
     * @param src 要压缩的文件（可以是文件，可以是文件夹，但是必须存在）
     * @param dst 压缩完成后写入的文件（必须不存在）
     */
    fun zip(src: File, dst: File){
        if (!src.exists()){
            throw Exception("源文件不存在 ${src.absolutePath}")
        }
        if (dst.exists()){
            throw Exception("目标文件已经存在 ${dst.absolutePath}")
        }
        // 创建父目录
        val dstParent = dst.parentFile
        if (!dstParent.exists()){
            if (!dstParent.mkdirs()){
                throw Exception("创建文件失败，目标文件的上一级目录创建失败")
            }
        }
        // 创建文件
        dst.createNewFile()
        // 递归压缩
        val zipStream = ZipOutputStream(dst.outputStream())
        try {
            zipIntoStream(src, zipStream, src.name)
        } finally {
            zipStream.flush()
            zipStream.close()
        }
    }

    /**
     * 把文件压缩到Zip文件流中
     * @param srcFile 文件或文件夹
     * @param stream Zip文件输出流
     */
    private fun zipIntoStream(srcFile: File, stream: ZipOutputStream, name: String){
        println("正在压缩 ${srcFile.path}")
        if (!srcFile.exists()){
            return
        }
        if (srcFile.isFile){
            stream.putNextEntry(ZipEntry(name))
            stream.write(srcFile.readBytes())
            stream.closeEntry()
        } else {
            val listFile = srcFile.listFiles() ?: return
            if (listFile.isEmpty()){
                // 空文件夹
                stream.putNextEntry(ZipEntry("${name}/"))
                stream.closeEntry()
            } else {
                // 含有文件或文件夹，递归压缩
                for (tmpFile in listFile){
                    zipIntoStream(tmpFile, stream, "$name/${tmpFile.name}")
                }
            }
        }
    }

}