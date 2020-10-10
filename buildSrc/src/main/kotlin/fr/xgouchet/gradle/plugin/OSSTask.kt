package fr.xgouchet.gradle.plugin

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.TaskAction
import java.io.File

open class OSSTask : DefaultTask() {

    private val provider: OSSLicenseProvider = OSSLicenseProvider()

    init {
        group = "oss"
        description = "Lists Third Party Licences in a json file in the assets folder"
    }

    @TaskAction
    fun applyTask() {
        val dependencies = provider.listOSSDependencies(project)
        val outputFile = getOutputFile()

        outputFile.printWriter().use { writer ->
            writer.println(
                    dependencies.joinToString(
                            separator = ",\n    ",
                            prefix = "[\n    ",
                            postfix = "\n]"
                    ) { it.toJson() }
            )
        }
    }

    @OutputFile
    fun getOutputFile(): File {
        val srcDir = File(project.projectDir, "src")
        val mainDir = File(srcDir, "main")
        val assetsDir = File(mainDir, "assets")
        return File(assetsDir, "oss_licenses.json")
    }

}