package fr.xgouchet.packageexplorer.oss

import android.content.Intent
import fr.xgouchet.packageexplorer.details.adapter.AppInfoViewModel
import fr.xgouchet.packageexplorer.ui.mvp.BaseActivity

class OSSActivity :
        BaseActivity<Any, List<AppInfoViewModel>, OSSPresenter, OSSFragment>() {

    override val allowNullIntentData: Boolean = true

    override fun readIntent(intent: Intent): Any? {
        return null
    }

    override fun instantiatePresenter(): OSSPresenter {
        return OSSPresenter(this, UrlNavigator())
    }

    override fun instantiateFragment(): OSSFragment {
        return OSSFragment()
    }

    override fun getPresenterKey(): String {
        return "OSS"
    }
}
