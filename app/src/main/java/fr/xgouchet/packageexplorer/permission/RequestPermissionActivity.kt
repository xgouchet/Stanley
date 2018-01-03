package fr.xgouchet.packageexplorer.permission

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import fr.xgouchet.packageexplorer.R
import fr.xgouchet.packageexplorer.details.apk.ApkDetailsActivity


class RequestPermissionActivity : AppCompatActivity() {

    companion object {
        const val PERMISSION_REQUEST_READ_STORAGE = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_permissions)

        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {

            Snackbar.make(findViewById(R.id.root_content), "To be able to open this APK, we need permission to read your external storage. Please.", Snackbar.LENGTH_INDEFINITE)
                    .setAction(android.R.string.ok, {
                        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), PERMISSION_REQUEST_READ_STORAGE)
                    })
                    .show()

        } else {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), PERMISSION_REQUEST_READ_STORAGE)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            PERMISSION_REQUEST_READ_STORAGE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    val apkIntent = Intent(this, ApkDetailsActivity::class.java)
                    apkIntent.data = intent.data
                    startActivity(apkIntent)
                    finish()
                } else {
                    Toast.makeText(this, "Unable to read APK file without that permission.", Toast.LENGTH_LONG).show()
                    finish()
                }
            }
        }
    }

}