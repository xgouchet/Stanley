package fr.xgouchet.packageexplorer.details.apk

import android.content.Context
import android.net.Uri
import fr.xgouchet.packageexplorer.core.utils.getFileName
import io.reactivex.rxjava3.core.SingleEmitter
import io.reactivex.rxjava3.core.SingleOnSubscribe
import timber.log.Timber
import java.io.File
import java.io.IOException

class CopyApkSource(
    val context: Context,
    val uri: Uri
) : SingleOnSubscribe<String> {

    override fun subscribe(emitter: SingleEmitter<String>) {
        try {
            val path = makeLocalCopy(uri)
            if (path != null) {
                emitter.onSuccess(path)
            } else {
                emitter.onError(NullPointerException("Oups"))
            }
        } catch (e: IOException) {
            Timber.e("Error copying the apk locally", e)
            emitter.onError(e)
        }
    }

    private fun makeLocalCopy(uri: Uri): String? {
        val cacheFolder = context.cacheDir
        val filename = uri.getFileName(context) ?: "unknown"
        val outPath = File(cacheFolder, filename)
        val input = context.contentResolver.openInputStream(uri) ?: return null

        input.use { i -> outPath.outputStream().use { i.copyTo(it) } }

        return outPath.absolutePath
    }
}
