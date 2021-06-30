package com.mj93.samefuncplugin

import com.android.build.api.transform.QualifiedContent
import com.android.build.api.transform.Transform
import com.android.build.api.transform.TransformInvocation
import com.android.build.gradle.internal.pipeline.TransformManager
import com.google.gson.Gson
import com.mj93.samefuncplugin.util.TransformHelper
import java.io.File
import java.io.PrintWriter

/**
 * @Title:
 * @Description: (用一句话描述该文件做什么)
 * @author liumengjie
 * @date 2021/6/29 17:57
 * @version V1.0
 */
class AppExtTransform(val appExt: AppExt) : Transform() {
    override fun getName(): String {
        return "SameFuncPlugin"
    }

    override fun getInputTypes(): MutableSet<QualifiedContent.ContentType> {
        return TransformManager.CONTENT_CLASS
    }

    override fun getScopes(): MutableSet<in QualifiedContent.Scope> {
        return TransformManager.SCOPE_FULL_PROJECT
    }

    override fun isIncremental(): Boolean {
        return true
    }


    override fun transform(transformInvocation: TransformInvocation?) {
        super.transform(transformInvocation)
        Const.samesList.clear()
        Const.samesMap.clear()
        Const.appExt=appExt

        if (transformInvocation != null) {
            transformInvocation.outputProvider.deleteAll()
            for (transformInput in transformInvocation.inputs) {
                for (jarInput in transformInput.jarInputs) {
                    TransformHelper.transformJars(
                        jarInput,
                        transformInvocation.outputProvider,
                        transformInvocation.isIncremental
                    )
                }
                for (directoryInput in transformInput.directoryInputs) {
                    TransformHelper.transformDirectory(
                        directoryInput,
                        transformInvocation.outputProvider,
                        transformInvocation.isIncremental
                    )
                }
            }
        }
        println("samefunc:count:${Const.samesMap.size} complete")
        var file = File(appExt.sameFuncOutputFilePath+"/"+appExt.sameFuncOutputFileName)
        val printWriter = PrintWriter(file)
        printWriter.write(Gson().toJson(Const.samesMap))
        printWriter.close()
    }

}