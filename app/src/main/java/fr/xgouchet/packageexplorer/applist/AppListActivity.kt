package fr.xgouchet.packageexplorer.applist

import android.content.Intent
import android.os.Bundle
import fr.xgouchet.packageexplorer.R
import fr.xgouchet.packageexplorer.ui.mvp.BaseActivity

class AppListActivity
    : BaseActivity<String, List<AppViewModel>, AppListPresenter, AppListFragment>() {

    override val allowNullIntentData: Boolean = true

    override fun readIntent(intent: Intent): String? {
        return null
    }

    override fun instantiatePresenter(): AppListPresenter {
        return AppListPresenter(this)
    }

    override fun instantiateFragment(): AppListFragment {
        return AppListFragment()
    }

    override fun getPresenterKey(): String {
        return "app_list"
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        setTitle(R.string.activity_title_appList)
    }

}