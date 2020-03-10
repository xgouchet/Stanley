package fr.xgouchet.packageexplorer.details.apk

import android.content.Context
import android.net.Uri
import fr.xgouchet.packageexplorer.core.utils.getFileName
import io.reactivex.SingleEmitter
import io.reactivex.SingleOnSubscribe
import java.io.File

class CopyApkSource(
    val context: Context,
    val uri: Uri
) :
    SingleOnSubscribe<String> {

    override fun subscribe(emitter: SingleEmitter<String>) {
        try {
            val path = makeLocalCopy(uri)
            if (path != null) {
                emitter.onSuccess(path)
            } else {
                emitter.onError(NullPointerException("Oups"))
            }
        } catch (e: Exception) {
            e.printStackTrace()
            emitter.onError(e)
        }
    }

    private fun makeLocalCopy(uri: Uri): String? {
        val cacheFolder = context.cacheDir
        val filename = uri.getFileName(context) ?: "unknown"
        val outPath = File(cacheFolder, filename)
        val input = context.contentResolver.openInputStream(uri)

        input.use { i -> outPath.outputStream().use { i.copyTo(it) } }

        return outPath.absolutePath
    }
}
