package fr.xgouchet.packageexplorer.details.apk

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import fr.xgouchet.packageexplorer.core.utils.getFileName
import fr.xgouchet.packageexplorer.details.AppInfoViewModel
import fr.xgouchet.packageexplorer.ui.mvp.BaseActivity


/**
 * @author Xavier F. Gouchet
 */
class ApkDetailsActivity
    : BaseActivity<Uri, List<AppInfoViewModel>, ApkDetailsPresenter, ApkDetailsFragment>() {


    override fun readIntent(intent: Intent): Uri? {
        return intent.data
    }

    override fun instantiatePresenter(): ApkDetailsPresenter {
        return ApkDetailsPresenter(this, intentData)
    }

    override fun instantiateFragment(): ApkDetailsFragment {
        return ApkDetailsFragment()
    }

    override fun getPresenterKey(): String {
        return "apk_details/$intentData"
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        intentData.let {
            title = it.getFileName(this)
        }
    }

//    internal lateinit var uri: Uri
//    internal var apkDetailsPresenter: ApkDetailsPresenter by notNull()
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//
//        setContentView(R.layout.activity_apk_details)
//
//        readUriFromIntent()
//
//        if (uri.scheme == "file") {
//            val permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
//            if (permissionCheck == PermissionChecker.PERMISSION_DENIED) {
//                val permissionIntent = Intent(this, RequestPermissionActivity::class.java)
//                permissionIntent.data = intent.data
//                startActivity(permissionIntent)
//                finish()
//            }
//        }
//        if (isFinishing) return
//
//        setupHeader()
//
//        setupMVP()
//    }
//
//    private fun readUriFromIntent() {
//        val intent = intent
//
//        val dataUri = intent.data
//        if (dataUri == null) {
//            finish()
//            return
//        }
//
//        uri = dataUri
//    }
//
//
//    private fun setupHeader() {
//        val toolbar = findViewById<Toolbar>(R.id.toolbar)
//        setSupportActionBar(toolbar)
//
//        uri.let {
//            title = it.getFileName(this)
//        }
//
//    }
//
//    private fun setupMVP() {
//        val fragment = supportFragmentManager.findFragmentById(R.id.fragment_container) as ApkDetailsFragment
//
//        apkDetailsPresenter = getExistingPresenter(fragment) ?: ApkDetailsPresenter(fragment, this, uri)
//    }
//
//    private fun getExistingPresenter(fragment: ApkDetailsFragment): ApkDetailsPresenter? {
//        val lastPresenter = lastCustomNonConfigurationInstance as ApkDetailsPresenter? ?: return null
//
//        lastPresenter.displayer = fragment
//        return lastPresenter
//    }

}