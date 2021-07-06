package top.mj93.samemethod.util

import top.mj93.samemethod.AppExt
import top.mj93.samemethod.Const
import java.io.File
import java.io.FileOutputStream
import java.io.PrintWriter

/**
 * @Title:
 * @Description: 输出日志
 * @author liumengjie
 * @date 2021/7/5 15:32
 * @version V1.0
 */
object LogUtils {


    fun println(content:String,isAppend:Boolean=true){
        val file = File(Const.appExt!!.sameFuncOutputFilePath + "/output.txt")
        val printWriter = PrintWriter(FileOutputStream(file,isAppend))
        printWriter.println(content)
        printWriter.close()
    }
}