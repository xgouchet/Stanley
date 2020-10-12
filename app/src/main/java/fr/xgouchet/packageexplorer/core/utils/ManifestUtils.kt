package fr.xgouchet.packageexplorer.core.utils

import android.content.Context
import android.content.pm.PackageInfo
import fr.xgouchet.axml.CompressedXmlParser
import io.reactivex.Observable
import java.io.File
import java.io.FileNotFoundException
import java.io.InputStream
import java.util.Enumeration
import java.util.zip.ZipEntry
import java.util.zip.ZipFile
import javax.xml.transform.OutputKeys
import javax.xml.transform.TransformerFactory
import javax.xml.transform.dom.DOMSource
import javax.xml.transform.stream.StreamResult
import org.w3c.dom.Document

const val MANIFEST_FILE_NAME = "AndroidManifest.xml"

fun exportManifestFromPackage(
    info: PackageInfo,
    context: Context
):
        Observable<File> {
    return Observable.fromCallable({
        val name = exportedManifestName(info.packageName)
        val apk = getPackageApk(info)
        return@fromCallable exportManifestFromApkFile(name, apk, context)
    })
}

fun exportManifestDomFromPackage(
        info: PackageInfo
): Document {
    return parseManifestFile(getPackageApk(info))
}

fun exportManifestFromApk(
    apk: File,
    context: Context
):
        Observable<File> {
    return Observable.fromCallable({
        val name = "${apk.nameWithoutExtension}_AndroidManifest.xml"

        return@fromCallable exportManifestFromApkFile(name, apk, context)
    })
}

private fun exportManifestFromApkFile(name: String, apk: File, context: Context): File {
    val doc = parseManifestFile(apk)
    val destFile = File(context.cacheDir, name)
    writeXml(doc, destFile)
    return destFile
}

private fun writeXml(doc: Document, output: File) {
    val transformerFactory = TransformerFactory.newInstance()
    val transformer = transformerFactory.newTransformer()
    transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8")
    transformer.setOutputProperty(OutputKeys.INDENT, "yes")
    transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2")
    val source = DOMSource(doc)
    val result = StreamResult(output)
    transformer.transform(source, result)
}

private fun exportedManifestName(packageName: String): String {
    val cleaned = packageName.replace("\\.".toRegex(), "_")
    return "${cleaned}_AndroidManifest.xml"
}

private fun getPackageApk(info: PackageInfo): File {
    val srcPackage = info.applicationInfo.publicSourceDir
    return File(srcPackage)
}

private fun parseManifestFile(apkFile: File): Document {
    var zipFile: ZipFile? = null
    var inputStream: InputStream? = null
    var doc: Document? = null

    try {
        zipFile = ZipFile(apkFile)
        val entries: Enumeration<out ZipEntry> = zipFile.entries()

        val parser = CompressedXmlParser()

        while (entries.hasMoreElements()) {
            val entry = entries.nextElement()
            if (entry.name == MANIFEST_FILE_NAME) {

                inputStream = zipFile.getInputStream(entry)
                doc = parser.parseDOM(inputStream)
                break
            }
        }
    } finally {
        inputStream?.close()
        zipFile?.close()
    }

    return doc ?: throw FileNotFoundException("Couldn't file manifest in apk")
}
