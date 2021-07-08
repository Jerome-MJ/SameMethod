package top.mj93.samemethod.visitor

import org.objectweb.asm.ClassReader
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.Opcodes
import org.objectweb.asm.tree.*
import org.objectweb.asm.util.Printer
import org.objectweb.asm.util.Textifier
import org.objectweb.asm.util.TraceMethodVisitor
import top.mj93.samemethod.AppExt
import top.mj93.samemethod.Const
import top.mj93.samemethod.bean.SameFuncBean
import top.mj93.samemethod.util.LogUtils
import top.mj93.samemethod.util.StrUtils
import java.io.PrintWriter
import java.io.StringWriter


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
        this.className = className!!.replace("/".toRegex(), ".").replace("\\\\".toRegex(), ".")
        if(appExt?.sameFuncSwitch!!){
            if(appExt.filterContainsName!=null){
                for(s in appExt.filterContainsName!!){
                    if(className!!.contains(s,true))return
                }
            }

            var isContains=false
            if(appExt.filterListName!=null){
                for(s in appExt.filterListName!!){
                    isContains = className!!.contains(s, true)
                }
                if(!isContains){
                    return
                }
            }
            LogUtils.println(className!!)
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
                if(inList.size()==0)return
                if (isEmptyMethod(n) || isGetSetMethod(n) || isSingleMethod(n)) {
                    continue
                }
                if (n.name.contains("toString"))continue
                if ("<init>" == n.name || "<clinit>" == n.name || n.name.startsWith("access$")) { continue }
                sb.append(n.name)
                for (i in inList) {
                    if (i is LineNumberNode) continue
                    if (i is LabelNode) continue
                    if (i is LdcInsnNode) continue
                    val insnToString = insnToString(i)
                    if(insnToString!!.length<20){
                        continue
                    }
                    sb.append(insnToString)
                }
                val replace = sb.toString()
//                val replace = sb.toString()
                for (s in Const.samesList) {
                    if("${className}_${n.name}" == "${s.className}_${s.funcName}")continue
                    var len = StrUtils.AnotherCompare(s.content, replace)
                    Const.COMPARE_COUNT++
                    if (len > appExt!!.sameFuncRadio) {
                        val key = "${s.className}_${s.funcName}"
                        LogUtils.println("${key}-----${className}_${n.name}的相似度：" + len)
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
                sb.delete(0,sb.length)
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

    private fun isEmptyMethod(methodNode: MethodNode): Boolean {
        val iterator: ListIterator<AbstractInsnNode> = methodNode.instructions.iterator()
        while (iterator.hasNext()) {
            val insnNode = iterator.next()
            val opcode = insnNode.opcode
            if (-1 != opcode) {
                return false
            }
        }
        //System.out.println("FilterMethodVisitor : filter -----> isEmptyMethod:" + name);
        return true
    }
    private fun isSingleMethod(methodNode: MethodNode): Boolean {
        val iterator: ListIterator<AbstractInsnNode> = methodNode.instructions.iterator()
        while (iterator.hasNext()) {
            val insnNode = iterator.next()
            val opcode = insnNode.opcode
            if (Opcodes.INVOKEVIRTUAL <= opcode && opcode <= Opcodes.INVOKEDYNAMIC) {
                return false
            }
        }
        //System.out.println("FilterMethodVisitor : filter -----> isSingleMethod:" + name);
        return true
    }
    private fun isGetSetMethod(methodNode: MethodNode): Boolean {
        val iterator: ListIterator<AbstractInsnNode> = methodNode.instructions.iterator()
        while (iterator.hasNext()) {
            val insnNode = iterator.next()
            val opcode = insnNode.opcode
            if (-1 == opcode) {
                continue
            }
            if (opcode != Opcodes.GETFIELD && opcode != Opcodes.GETSTATIC && opcode != Opcodes.H_GETFIELD && opcode != Opcodes.H_GETSTATIC && opcode != Opcodes.RETURN && opcode != Opcodes.ARETURN && opcode != Opcodes.DRETURN && opcode != Opcodes.FRETURN && opcode != Opcodes.LRETURN && opcode != Opcodes.IRETURN && opcode != Opcodes.PUTFIELD && opcode != Opcodes.PUTSTATIC && opcode != Opcodes.H_PUTFIELD && opcode != Opcodes.H_PUTSTATIC && opcode > Opcodes.SALOAD) {
                return false
            }
        }
        //System.out.println("FilterMethodVisitor : filter -----> isGetSetMethod:" + name);
        return true
    }
}