package top.mj93.samemethod

import com.android.build.gradle.AppExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import java.io.File

class SameFuncPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        var appExtTransform = project.extensions.findByType(AppExtension::class.java)
        val appExt = project.extensions.create("SameFuncExt", AppExt::class.java)
        if(appExt.sameFuncOutputFilePath==null){
            appExt.sameFuncOutputFilePath=project.buildFile.path+"/samefunc"
        }
        val file = File(appExt.sameFuncOutputFilePath)
        if(!file.exists()){
            file.mkdir()
        }
        appExtTransform?.registerTransform(AppExtTransform(appExt))
    }
}

//fun main() {
//
//    printSameFun(IOUtils::class.java.name)
//
//}
//
//fun printSameFun(className: String) {
//    try {
//        val classNode = ClassNode()
//        var classReader = ClassReader(A::class.java.name)
//        classReader.accept(classNode, 0)
//        var methods = classNode.methods
//
//        var sb = StringBuffer()
//        for (n in methods) {
//            val inList: InsnList = n.instructions
//            if(inList.size()==0)return
//            if (isEmptyMethod(n) || isGetSetMethod(n) || isSingleMethod(n)) {
//                continue
//            }
//            if(n.invisibleAnnotations!=null&&n.invisibleAnnotations.isNotEmpty())continue
//            if (n.name.contains("toString"))continue
//            if ("<init>" == n.name || "<clinit>" == n.name || n.name.startsWith("access$")) { continue }
//            sb.append(n.name)
//            println(n.name)
//            for (i in inList) {
//                if (i is LineNumberNode) continue
//                if (i is LabelNode) continue
//                if (i is LdcInsnNode) continue
//                val insnToString = insnToString(i)
//                if(insnToString!!.length<20){
//                    continue
//                }
//                sb.append(insnToString)
//            }
////                val replace = sb.toString().replace(" ", "").replace("\n", "")
//            val replace = sb.toString()
//            var returnType = n.desc.substring(n.desc.lastIndexOf(")")+1)
//            for (s in Const.samesList) {
//                if("${className}_${n.name}" == "${s.className}_${s.funcName}")continue
//                if(returnType != s.returnType)continue
//                var len = StrUtils.AnotherCompare(s.content, replace)
//                Const.COMPARE_COUNT++
//                if (len > 0.8) {
//                    val key = "${s.className}_${s.funcName}"
//                    LogUtils.println("${key}-----${className}_${n.name}的相似度：" + len)
//                    LogUtils.println("存储字段:${s.content}")
//                    LogUtils.println("当前字段:${replace}")
//                    if (Const.samesMap.containsKey(key)) {
//                        Const.samesMap[key]?.add("${className}_${n.name}")
//                    } else {
//                        val tempSameList = mutableListOf<String>()
//                        tempSameList.add(key)
//                        tempSameList.add("${className}_${n.name}")
//                        Const.samesMap[key] = tempSameList
//                    }
//                    break
//                }
//            }
//            sb.delete(0,sb.length)
//            var sambean = SameFuncBean(className, n.name, replace,returnType)
//            Const.samesList.add(sambean)
//        }
//    } catch (e: Exception) {
//        e.printStackTrace()
//    }
//
//}
//
//fun insnToString(insn: AbstractInsnNode): String? {
//    insn.accept(mp)
//    val sw = StringWriter()
//    printer.print(PrintWriter(sw))
//    printer.getText().clear()
//    return sw.toString()
//}
//
//private val printer: Printer = Textifier()
//private val mp: TraceMethodVisitor = TraceMethodVisitor(printer)
//
//private fun isEmptyMethod(methodNode: MethodNode): Boolean {
//    val iterator: ListIterator<AbstractInsnNode> = methodNode.instructions.iterator()
//    while (iterator.hasNext()) {
//        val insnNode = iterator.next()
//        val opcode = insnNode.opcode
//        if (-1 != opcode) {
//            return false
//        }
//    }
//    //System.out.println("FilterMethodVisitor : filter -----> isEmptyMethod:" + name);
//    return true
//}
//private fun isSingleMethod(methodNode: MethodNode): Boolean {
//    val iterator: ListIterator<AbstractInsnNode> = methodNode.instructions.iterator()
//    while (iterator.hasNext()) {
//        val insnNode = iterator.next()
//        val opcode = insnNode.opcode
//        if (Opcodes.INVOKEVIRTUAL <= opcode && opcode <= Opcodes.INVOKEDYNAMIC) {
//            return false
//        }
//    }
//    //System.out.println("FilterMethodVisitor : filter -----> isSingleMethod:" + name);
//    return true
//}
//private fun isGetSetMethod(methodNode: MethodNode): Boolean {
//    val iterator: ListIterator<AbstractInsnNode> = methodNode.instructions.iterator()
//    while (iterator.hasNext()) {
//        val insnNode = iterator.next()
//        val opcode = insnNode.opcode
//        if (-1 == opcode) {
//            continue
//        }
//        if (opcode != Opcodes.GETFIELD && opcode != Opcodes.GETSTATIC && opcode != Opcodes.H_GETFIELD && opcode != Opcodes.H_GETSTATIC && opcode != Opcodes.RETURN && opcode != Opcodes.ARETURN && opcode != Opcodes.DRETURN && opcode != Opcodes.FRETURN && opcode != Opcodes.LRETURN && opcode != Opcodes.IRETURN && opcode != Opcodes.PUTFIELD && opcode != Opcodes.PUTSTATIC && opcode != Opcodes.H_PUTFIELD && opcode != Opcodes.H_PUTSTATIC && opcode > Opcodes.SALOAD) {
//            return false
//        }
//    }
//    //System.out.println("FilterMethodVisitor : filter -----> isGetSetMethod:" + name);
//    return true
//}