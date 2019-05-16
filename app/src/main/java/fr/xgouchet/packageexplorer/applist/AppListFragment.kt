package fr.xgouchet.packageexplorer.applist

import android.app.Activity
import android.content.Intent
import android.content.pm.ResolveInfo
import android.net.Uri
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.widget.SearchView
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import com.google.android.gms.oss.licenses.OssLicensesMenuActivity
import fr.xgouchet.packageexplorer.R
import fr.xgouchet.packageexplorer.about.AboutActivity
import fr.xgouchet.packageexplorer.applist.sort.AppSort
import fr.xgouchet.packageexplorer.details.apk.ApkDetailsActivity
import fr.xgouchet.packageexplorer.launcher.LauncherDialog
import fr.xgouchet.packageexplorer.ui.adapter.BaseAdapter
import fr.xgouchet.packageexplorer.ui.mvp.list.BaseListFragment
import io.reactivex.functions.Consumer


class AppListFragment
    : BaseListFragment<AppViewModel, AppListPresenter>(),
        Consumer<AppViewModel> {

    override val adapter: BaseAdapter<AppViewModel> = AppAdapter(this, this)
    override val isFabVisible: Boolean = false
    override val fabIconOverride: Int? = null

    // region Fragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.app_list, menu)

        menu?.findItem(R.id.action_search)?.let {
            val searchView = it.actionView as SearchView
            searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean = false

                override fun onQueryTextChange(newText: String?): Boolean {
                    presenter.setFilter(newText ?: "")
                    return true
                }
            })
        }

        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onPrepareOptionsMenu(menu: Menu?) {
        super.onPrepareOptionsMenu(menu)
        val systemAppsVisible = presenter.areSystemAppsVisible()
        menu?.apply {
            findItem(R.id.hide_system_apps).isVisible = systemAppsVisible
            findItem(R.id.show_system_apps).isVisible = !systemAppsVisible
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        when (item?.itemId) {
            R.id.sort_by_title -> presenter.setSort(AppSort.TITLE)
            R.id.sort_by_package_name -> presenter.setSort(AppSort.PACKAGE_NAME)
            R.id.sort_by_install_time -> presenter.setSort(AppSort.INSTALL_TIME)
            R.id.sort_by_update_time -> presenter.setSort(AppSort.UPDATE_TIME)
            R.id.open_apk -> {
                val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
                intent.type = APK_MIME_TYPE
                intent.addCategory(Intent.CATEGORY_OPENABLE)
                startActivityForResult(intent, OPEN_APK_REQUEST)
            }
            R.id.hide_system_apps -> presenter.setSystemAppsVisible(false)
            R.id.show_system_apps -> presenter.setSystemAppsVisible(true)
            R.id.about -> {
                startActivity(Intent(activity, AboutActivity::class.java))
            }
            R.id.licenses -> {
                OssLicensesMenuActivity.setActivityTitle(getString(R.string.activity_title_licenses))
                startActivity(Intent(activity, OssLicensesMenuActivity::class.java))
            }
            else -> return super.onOptionsItemSelected(item)
        }
        return true
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            OPEN_APK_REQUEST -> onOpenApkResult(resultCode, data)
        }
    }

    // endregion

    // region Displayer

    fun promptActivity(resolvedInfos: List<ResolveInfo>) {
        val supportFragmentManager = activity?.supportFragmentManager ?: return
        val transaction = supportFragmentManager.beginTransaction()
        LauncherDialog.withData(resolvedInfos)
                .show(transaction, null)
    }

    // endregion

    // region Consumer

    override fun accept(t: AppViewModel) {
        presenter.openApplication(t.packageName)
    }

    // endregion

    // region Private

    private fun onOpenApkResult(resultCode: Int, resultData: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            val uri: Uri? = resultData?.data
            if (uri != null) {
                val intent = Intent(context, ApkDetailsActivity::class.java)
                intent.data = uri
                startActivity(intent)
            }
        }
    }

    // endregion

    companion object {
        const val APK_MIME_TYPE = "application/vnd.android.package-archive"
        const val OPEN_APK_REQUEST = 1
    }
}
