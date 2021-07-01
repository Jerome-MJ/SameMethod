package com.mj93.samemethod

import com.android.build.gradle.AppExtension
import org.gradle.api.Plugin
import org.gradle.api.Project

class SameFuncPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        println("hello samefuncplugin")
        var appExtTransform = project.extensions.findByType(AppExtension::class.java)
        val appExt = project.extensions.create("SameFuncExt", AppExt::class.java)
        appExtTransform?.registerTransform(AppExtTransform(appExt))
    }
}