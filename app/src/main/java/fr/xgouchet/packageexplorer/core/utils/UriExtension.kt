package fr.xgouchet.packageexplorer.core.utils

import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.OpenableColumns
import java.io.File

fun Uri.getFileName(context: Context): String? {

    if (scheme == "file"){
        return File(path).name
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