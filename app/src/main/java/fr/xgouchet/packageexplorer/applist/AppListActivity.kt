package fr.xgouchet.packageexplorer.applist

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import fr.xgouchet.packageexplorer.R
import fr.xgouchet.packageexplorer.ui.mvp.BaseActivity

class AppListActivity
    : BaseActivity<String, List<AppViewModel>, AppListPresenter, AppListFragment>() {

    override val allowNullIntentData: Boolean = true

    private val appListChangedReceiver = AppListChangedReceiver { presenter.load(true) }

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

    override fun onStart() {
        super.onStart()
        val intentFilter = IntentFilter()
        intentFilter.addAction(Intent.ACTION_PACKAGE_REMOVED)
        intentFilter.addAction(Intent.ACTION_PACKAGE_FULLY_REMOVED)
        intentFilter.addAction(Intent.ACTION_PACKAGE_ADDED)
        intentFilter.addAction(Intent.ACTION_PACKAGE_REPLACED)
        intentFilter.addDataScheme("package")
        registerReceiver(appListChangedReceiver, intentFilter)
    }

    override fun onStop() {
        super.onStop()
        unregisterReceiver(appListChangedReceiver)
    }


    class AppListChangedReceiver(private val onUninstalled: () -> Unit)
        : BroadcastReceiver() {

        override fun onReceive(context: Context, intent: Intent) {
            onUninstalled()
        }
    }

}