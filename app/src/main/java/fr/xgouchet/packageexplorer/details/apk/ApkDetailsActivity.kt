package fr.xgouchet.packageexplorer.details.apk

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v4.content.PermissionChecker
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import fr.xgouchet.packageexplorer.R
import fr.xgouchet.packageexplorer.core.utils.getFileName
import fr.xgouchet.packageexplorer.permission.RequestPermissionActivity
import kotlin.properties.Delegates.notNull


/**
 * @author Xavier F. Gouchet
 */
class ApkDetailsActivity : AppCompatActivity() {

    internal lateinit var uri: Uri
    internal var apkDetailsPresenter: ApkDetailsPresenter by notNull()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_apk_details)

        readUriFromIntent()

        if (uri.scheme == "file") {
            val permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
            if (permissionCheck == PermissionChecker.PERMISSION_DENIED) {
                val permissionIntent = Intent(this, RequestPermissionActivity::class.java)
                permissionIntent.data = intent.data
                startActivity(permissionIntent)
                finish()
            }
        }
        if (isFinishing) return

        setupHeader()

        setupMVP()
    }

    private fun readUriFromIntent() {
        val intent = intent

        val dataUri = intent.data
        if (dataUri == null) {
            finish()
            return
        }

        uri = dataUri
    }


    private fun setupHeader() {
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        uri.let {
            title = it.getFileName(this)
        }

    }

    private fun setupMVP() {
        val fragment = supportFragmentManager.findFragmentById(R.id.fragment_container) as ApkDetailsFragmentBase

        apkDetailsPresenter = getExistingPresenter(fragment) ?: ApkDetailsPresenter(fragment, this, uri)
    }

    private fun getExistingPresenter(fragment: ApkDetailsFragmentBase): ApkDetailsPresenter? {
        val lastPresenter = lastCustomNonConfigurationInstance as ApkDetailsPresenter? ?: return null

        lastPresenter.displayer = fragment
        return lastPresenter
    }

}