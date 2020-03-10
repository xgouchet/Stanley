package fr.xgouchet.packageexplorer.details.apk

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import fr.xgouchet.packageexplorer.core.utils.getFileName
import fr.xgouchet.packageexplorer.details.CertificateNavigator
import fr.xgouchet.packageexplorer.details.adapter.AppInfoViewModel
import fr.xgouchet.packageexplorer.ui.mvp.BaseActivity

/**
 * @author Xavier F. Gouchet
 */
class ApkDetailsActivity :
    BaseActivity<Uri, List<AppInfoViewModel>, ApkDetailsPresenter, ApkDetailsFragment>() {

    override val allowNullIntentData: Boolean = false

    override fun readIntent(intent: Intent): Uri? {
        return intent.data
    }

    override fun instantiatePresenter(): ApkDetailsPresenter {
        val uri = intentData ?: throw IllegalStateException("Intent data should not be null")
        return ApkDetailsPresenter(this, CertificateNavigator(), uri)
    }

    override fun instantiateFragment(): ApkDetailsFragment {
        return ApkDetailsFragment()
    }

    override fun getPresenterKey(): String {
        return "apk_details/$intentData"
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        intentData?.let {
            title = it.getFileName(this)
        }
    }
}
