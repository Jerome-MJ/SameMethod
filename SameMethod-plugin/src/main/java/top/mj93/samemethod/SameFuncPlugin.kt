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
            File(appExt.sameFuncOutputFilePath).mkdir()
        }
        appExtTransform?.registerTransform(AppExtTransform(appExt))
    }
}