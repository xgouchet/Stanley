package fr.xgouchet.gradle.plugin

import com.android.build.gradle.internal.tasks.factory.dependsOn
import org.gradle.api.Plugin
import org.gradle.api.Project

class OSSPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        target.tasks.create(TASK_UPDATE_NAME, OSSTask::class.java)
        target.tasks.named("preBuild").dependsOn(TASK_UPDATE_NAME)
    }

    companion object {
        const val TASK_UPDATE_NAME = "updateOSSLicences"
    }
}
