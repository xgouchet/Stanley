package fr.xgouchet.packageexplorer.details.app

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import fr.xgouchet.packageexplorer.applist.AppViewModel
import fr.xgouchet.packageexplorer.details.CertificateNavigator
import fr.xgouchet.packageexplorer.details.adapter.AppInfoViewModel
import fr.xgouchet.packageexplorer.ui.mvp.BaseActivity

/**
 * @author Xavier F. Gouchet
 */
class AppDetailsActivity :
    BaseActivity<AppViewModel, List<AppInfoViewModel>, AppDetailsPresenter, AppDetailsFragment>() {

    companion object {

        val EXTRA_PACKAGE_NAME = "package_name"

        fun startWithApp(activity: Activity, app: AppViewModel) {
            val intent = Intent(activity, AppDetailsActivity::class.java)
            intent.putExtra(EXTRA_PACKAGE_NAME, app.packageName)
            activity.startActivity(intent)
        }
    }

    private var uninstallReceiver: UninstallReceiver? = null

    override val allowNullIntentData: Boolean = false

    override fun readIntent(intent: Intent): AppViewModel? {
        val packageName = intent.getStringExtra(EXTRA_PACKAGE_NAME)
        if (packageName.isNullOrBlank()) {
            return null
        }

        uninstallReceiver = UninstallReceiver(packageName) { finish() }

        return AppViewModel.fromPackageName(this, packageName)
    }

    override fun instantiatePresenter(): AppDetailsPresenter {
        val appViewModel = intentData ?: throw IllegalStateException("Expected non null app here")
        return AppDetailsPresenter(this, CertificateNavigator(), appViewModel.packageName, appViewModel.isSystemApp)
    }

    override fun instantiateFragment(): AppDetailsFragment {
        return AppDetailsFragment()
    }

    override fun getPresenterKey(): String {
        val appViewModel = intentData ?: throw IllegalStateException("Expected non null app here")
        return "app_details/${appViewModel.packageName}"
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        intentData?.let {
            title = it.title
            toolbar.subtitle = it.packageName
        }
    }

    override fun onStart() {
        super.onStart()
        uninstallReceiver?.let {
            val intentFilter = IntentFilter()
            intentFilter.addAction(Intent.ACTION_PACKAGE_REMOVED)
            intentFilter.addAction(Intent.ACTION_PACKAGE_FULLY_REMOVED)
            intentFilter.addDataScheme("package")
            registerReceiver(it, intentFilter)
        }
    }

    override fun onStop() {
        super.onStop()
        uninstallReceiver?.let {
            unregisterReceiver(uninstallReceiver)
        }
    }

    class UninstallReceiver(
        private val watchedPackageName: String,
        private val onUninstalled: () -> Unit
    ) :
        BroadcastReceiver() {

        override fun onReceive(context: Context, intent: Intent) {
            val data = intent.data?.schemeSpecificPart
            val isReplacing = intent.getBooleanExtra(Intent.EXTRA_REPLACING, false)
            if (data == watchedPackageName && !isReplacing) {
                onUninstalled()
            }
        }
    }
}
