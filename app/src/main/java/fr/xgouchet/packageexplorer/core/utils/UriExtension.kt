package fr.xgouchet.packageexplorer.core.utils

import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.OpenableColumns
import java.io.File

fun Uri.isFile(): Boolean {
    return scheme == "file"
}

fun Uri.isContent(): Boolean {
    return scheme == "content"
}

fun Uri.getFileName(context: Context): String? {

    if (isFile()) {
        return File(path).name
    }

    if (!isContent()) {
        return null
    }

    var cursor: Cursor? = null
    var name: String? = null
    try {
        cursor = context.contentResolver.query(this, null, null, null, null)
        cursor?.let {
            val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            if (cursor.moveToFirst()) {
                name = cursor.getString(nameIndex)
            }
        }
    } finally {
        cursor?.close()
    }

    return name
}
