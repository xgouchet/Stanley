package fr.xgouchet.gradle.plugin

import javax.xml.parsers.DocumentBuilderFactory
import org.gradle.api.Project
import org.gradle.api.artifacts.component.ComponentIdentifier
import org.gradle.api.artifacts.result.ComponentSelectionCause
import org.gradle.api.artifacts.result.ResolvedArtifactResult
import org.gradle.api.artifacts.result.ResolvedDependencyResult
import org.gradle.maven.MavenModule
import org.gradle.maven.MavenPomArtifact
import org.w3c.dom.Document

class OSSLicenseProvider {

    fun listOSSDependencies(project: Project): List<OSSDependency> {
        val dependencies = getAllConfigurationsDependencies(project)
        val pomFilesList = resolvePomFiles(project, dependencies)

        return listOSSDependencies(dependencies, pomFilesList)
                .toSet()
                .sortedBy { it.identifier }
    }

    // region Internal

    private fun getAllConfigurationsDependencies(
        project: Project
    ): List<ComponentIdentifier> {
        return project.configurations
                .filter { it.isCanBeResolved }
                .map { configuration ->
                    configuration.incoming.resolutionResult.allDependencies
                            .filterIsInstance<ResolvedDependencyResult>()
                            .filter { it.isRoot() }
                            .map { it.selected.id }
                }
                .filter { it.isNotEmpty() }
                .flatten()
    }

    private fun resolvePomFiles(
        project: Project,
        dependencyIds: List<ComponentIdentifier>
    ): Map<ComponentIdentifier, String> {
        return project.dependencies
                .createArtifactResolutionQuery()
                .withArtifacts(MavenModule::class.java, MavenPomArtifact::class.java)
                .forComponents(dependencyIds)
                .execute()
                .resolvedComponents
                .flatMap { result ->
                    result.getArtifacts(MavenPomArtifact::class.java)
                            .filterIsInstance<ResolvedArtifactResult>()
                            .map { result.id to it.file.absolutePath }
                }.toMap()
    }

    private fun listOSSDependencies(
        dependencies: List<ComponentIdentifier>,
        pomFilesList: Map<ComponentIdentifier, String>
    ): List<OSSDependency> {
        return dependencies.mapNotNull {
            val pomFilePath = pomFilesList[it]
            if (pomFilePath.isNullOrBlank()) {
                System.err.println("Missing pom.xml file for dependency $it")
                null
            } else {
                readLicenseFromPomFile(pomFilePath)
            }
        }
    }

    private fun readLicenseFromPomFile(path: String): OSSDependency? {
        println("Reading POM from $path")
        val document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(path)
        val identifier = readDependencyIdFromPomDocument(document)
        val licenseString = readLicenseFromPomDocument(document)
        val sourceUrl = readSourceUrlFromPomDocument(document)
        val name = readDependencyNameFromPomDocument(document)

        return if (name != null) {
            return OSSDependency(
                    name = name,
                    identifier = identifier,
                    license = licenseString ?: "Unknown",
                    sourceUrl = sourceUrl
            )
        } else {
            System.err.println("Missing groupId in $path")
            null
        }
    }

    private fun readDependencyIdFromPomDocument(document: Document): String {
        val groupIdNode = document.getElementsByTagName(TAG_GROUP_ID)
                .asSequence().firstOrNull()
        val groupId = groupIdNode?.textContent
        val artifactIdNode = document.getElementsByTagName(TAG_ARTIFACT_ID)
                .asSequence().firstOrNull()
        val artifactId = artifactIdNode?.textContent
        val versionNode = document.getElementsByTagName(TAG_VERSION)
                .asSequence().firstOrNull()
        val version = versionNode?.textContent
        return "$groupId:$artifactId:$version"
    }

    private fun readSourceUrlFromPomDocument(document: Document): String? {
        val urlNode = document.getElementsByTagName(TAG_URL)
                .asSequence().firstOrNull()
        return urlNode?.textContent
    }

    private fun readDependencyNameFromPomDocument(document: Document): String? {
        val nameNode = document.getElementsByTagName(TAG_NAME)
                .asSequence().firstOrNull()
        return nameNode?.textContent
    }

    private fun readLicenseFromPomDocument(document: Document): String? {
        val licencesNode = document.getElementsByTagName(TAG_LICENSES)
                .asSequence()
                .firstOrNull()
        val licenceNodes = licencesNode?.childNodes
                ?.asSequence()
                ?.filter { it.nodeName == TAG_LICENSE }
        val licenses = licenceNodes?.asSequence()
                ?.mapNotNull {
                    it.childNodes
                            .asSequence()
                            .firstOrNull { child -> child.nodeName == TAG_NAME }
                }
                ?.joinToString("/") { it.textContent }
        return licenses
    }

    @Suppress("UnstableApiUsage")
    private fun ResolvedDependencyResult.isRoot(): Boolean {
        return from.selectionReason.descriptions.any {
            it.cause == ComponentSelectionCause.ROOT
        }
    }

    // endregion

    companion object {
        private const val TAG_GROUP_ID = "groupId"
        private const val TAG_ARTIFACT_ID = "artifactId"
        private const val TAG_VERSION = "version"
        private const val TAG_LICENSES = "licenses"
        private const val TAG_LICENSE = "license"
        private const val TAG_NAME = "name"
        private const val TAG_URL = "url"
    }
}
