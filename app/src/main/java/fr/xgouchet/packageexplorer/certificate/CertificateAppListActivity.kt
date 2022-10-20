package fr.xgouchet.packageexplorer.certificate

import android.content.Context
import android.content.Intent
import android.os.Bundle
import fr.xgouchet.packageexplorer.applist.AppViewModel
import fr.xgouchet.packageexplorer.core.utils.humanReadableName
import fr.xgouchet.packageexplorer.ui.mvp.BaseActivity
import timber.log.Timber
import javax.security.cert.CertificateException
import javax.security.cert.X509Certificate

class CertificateAppListActivity :
    BaseActivity<X509Certificate, List<AppViewModel>, CertificateAppListPresenter, CertificateAppListFragment>() {

    companion object {
        const val EXTRA_CERTIFICATE_ENCODED = "certificate_encoded"

        @JvmStatic
        fun getIntent(certificate: X509Certificate, context: Context): Intent {
            val intent = Intent(context, CertificateAppListActivity::class.java)
            intent.putExtra(EXTRA_CERTIFICATE_ENCODED, certificate.encoded)
            return intent
        }
    }

    override val allowNullIntentData: Boolean = false

    override fun readIntent(intent: Intent): X509Certificate? {
        val encoded = intent.getByteArrayExtra(EXTRA_CERTIFICATE_ENCODED)

        val cert = try {
            X509Certificate.getInstance(encoded)
        } catch (e: CertificateException) {
            Timber.e("Can't get certificate from intent", e)
            null
        }
        return cert
    }

    override fun instantiatePresenter(): CertificateAppListPresenter {
        return CertificateAppListPresenter(this, intentData!!)
    }

    override fun instantiateFragment(): CertificateAppListFragment {
        return CertificateAppListFragment()
    }

    override fun getPresenterKey(): String {
        return "certificate_app_list"
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        title = intentData?.humanReadableName()
    }
}
