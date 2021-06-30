package com.mj93.samefuncplugin.visitor

import com.google.gson.Gson
import com.mj93.samefuncplugin.AppExt
import com.mj93.samefuncplugin.Const
import com.mj93.samefuncplugin.SameFuncPlugin
import com.mj93.samefuncplugin.bean.SameFuncBean
import com.mj93.samefuncplugin.util.StrUtils
import org.objectweb.asm.ClassReader
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes
import org.objectweb.asm.tree.*
import org.objectweb.asm.util.Printer
import org.objectweb.asm.util.Textifier
import org.objectweb.asm.util.TraceMethodVisitor
import java.io.File
import java.io.FileOutputStream
import java.io.PrintWriter
import java.io.StringWriter
import java.sql.Struct


/**
 * @Title:
 * @Description: (用一句话描述该文件做什么)
 * @author liumengjie
 * @date 2021/6/29 18:07
 * @version V1.0
 */
class SFClassVisitor(
    api: Int,
    var classReader: ClassReader,
    val appExt: AppExt?,
    classVisitor: ClassVisitor?
) :
    ClassVisitor(api, classVisitor) {

    private var className: String? = null
    override fun visit(
        version: Int,
        access: Int,
        name: String?,
        signature: String?,
        superName: String?,
        interfaces: Array<out String>?
    ) {
        super.visit(version, access, name, signature, superName, interfaces)
        this.className = name

        if(appExt?.sameFuncSwitch!!){
            if(appExt.filterName !=null){
                for(s in appExt.filterName!!){
                    if(s==className)return
                }
            }
            if(appExt.filterContainsName!=null){
                for(s in appExt.filterContainsName!!){
                    if(className!!.contains(s))return
                }
            }
            if(appExt.filterStartName!=null){
                for(s in appExt.filterStartName!!){
                    if(className!!.startsWith(s))return
                }
            }
            if(appExt.filterEndName!=null){
                for(s in appExt.filterEndName!!){
                    if(className!!.endsWith(s))return
                }
            }
            println(className)
            printSameFun(className!!)
        }

    }

    fun printSameFun(className: String) {
        try {
            val classNode = ClassNode()
            classReader.accept(classNode, 0)
            var methods = classNode.methods

            var sb = StringBuffer()
            for (n in methods) {
                val inList: InsnList = n.instructions
                if ("<init>".equals(n.name)) continue
                sb.append(n.name)
                for (i in inList) {
                    if (i is LineNumberNode) continue
                    if (i is LabelNode) continue
                    val insnToString = insnToString(i)
                    sb.append(insnToString)
                }
                val replace = sb.toString().replace(" ", "").replace("\n", "")
                for (s in Const.samesList) {
                    var len = StrUtils.AnotherCompare(s.content, replace)
                    if (len > appExt!!.sameFuncRadio) {
                        println("${s.content}-----${replace}的相似度：" + len)
                        val key = "${s.className}_${s.funcName}"
                        if (Const.samesMap.containsKey(key)) {
                            Const.samesMap[key]?.add("${className}_${n.name}")
                        } else {
                            val tempSameList = mutableListOf<String>()
                            tempSameList.add(key)
                            tempSameList.add("${className}_${n.name}")
                            Const.samesMap[key] = tempSameList
                        }
                        break
                    }
                }
                var sambean = SameFuncBean(className, n.name, replace)
                Const.samesList.add(sambean)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    fun insnToString(insn: AbstractInsnNode): String? {
        insn.accept(mp)
        val sw = StringWriter()
        printer.print(PrintWriter(sw))
        printer.getText().clear()
        return sw.toString()
    }

    private val printer: Printer = Textifier()
    private val mp: TraceMethodVisitor = TraceMethodVisitor(printer)


}