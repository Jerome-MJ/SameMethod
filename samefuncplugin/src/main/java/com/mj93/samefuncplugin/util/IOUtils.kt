package com.mj93.samefuncplugin.util

import java.io.*

/**
 * @Title:
 * @Description: (用一句话描述该文件做什么)
 * @author liumengjie
 * @date 2021/6/30 14:33
 * @version V1.0
 */
internal object IOUtils {

    fun readBytes(inputStream: InputStream): ByteArray? {
        val byteArrayOutputStream = ByteArrayOutputStream()
        try {
            val buffer = ByteArray(1024)
            var len: Int
            while (inputStream.read(buffer).apply { len = this } != -1) {
                byteArrayOutputStream.write(buffer, 0, len)
            }
            byteArrayOutputStream.flush()
            return byteArrayOutputStream.toByteArray()
        } catch (ex: Exception) {
            ex.printStackTrace()
        } finally {
            byteArrayOutputStream.close()
            inputStream.close()
        }
        return null
    }

    fun byte2File(outputPath: String, sourceByte: ByteArray) {
        val file = File(outputPath)
        if (file.exists()) {
            file.delete()
        }

        val inputStream = ByteArrayInputStream(sourceByte)
        val outputStream = FileOutputStream(file)
        val buffer = ByteArray(1024)
        var len: Int
        while (inputStream.read(buffer).apply { len = this } != -1) {
            outputStream.write(buffer, 0, len)
        }
        outputStream.flush()
        outputStream.close()
        inputStream.close()
    }
}