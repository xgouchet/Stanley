package fr.xgouchet.packageexplorer.details.app

import android.Manifest
import android.annotation.TargetApi
import android.content.Intent
import android.content.pm.ResolveInfo
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.core.content.FileProvider
import androidx.core.view.ViewCompat
import com.google.android.material.snackbar.Snackbar
import fr.xgouchet.packageexplorer.BuildConfig
import fr.xgouchet.packageexplorer.R
import fr.xgouchet.packageexplorer.details.adapter.AppDetailsAdapter
import fr.xgouchet.packageexplorer.details.adapter.AppInfoViewModel
import fr.xgouchet.packageexplorer.launcher.LauncherDialog
import fr.xgouchet.packageexplorer.ui.adapter.BaseAdapter
import fr.xgouchet.packageexplorer.ui.mvp.list.BaseListFragment
import io.reactivex.functions.Consumer
import java.io.File

/**
 * @author Xavier F. Gouchet
 */
class AppDetailsFragment :
        BaseListFragment<AppInfoViewModel, AppDetailsPresenter>() {

    override val adapter: BaseAdapter<AppInfoViewModel> = AppDetailsAdapter(this, Consumer { presenter.actionTriggered(it) })
    override val isFabVisible: Boolean = false
    override val fabIconOverride: Int? = null

    // region Fragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ViewCompat.setNestedScrollingEnabled(view.findViewById(android.R.id.list), false)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.app_details, menu)

        if (presenter.isSystemApp) {
            menu.findItem(R.id.action_uninstall)?.isVisible = false
            menu.findItem(R.id.action_play_store)?.isVisible = false
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_app_launch -> {
                presenter.openApplication()
                return true
            }
            R.id.action_app_info -> {
                presenter.openAppInfo()
                return true
            }
            R.id.action_play_store -> {
                presenter.openPlayStore()
                return true
            }
            R.id.action_uninstall -> {
                onUninstallRequested()
                return true
            }
            R.id.action_manifest -> {
                presenter.exportManifest()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    // endregion

    // region Displayer

    fun promptActivity(resolvedInfos: List<ResolveInfo>) {
        val supportFragmentManager = activity?.supportFragmentManager ?: return
        val transaction = supportFragmentManager.beginTransaction()
        LauncherDialog.withData(resolvedInfos)
                .show(transaction, null)
    }

    fun onManifestExported(dest: File) {
        val currentActivity = activity ?: return

        val intent = Intent(Intent.ACTION_VIEW)
        val uri = FileProvider.getUriForFile(currentActivity, BuildConfig.APPLICATION_ID, dest)
        intent.setDataAndType(uri, "text/xml")
        intent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION

        val resolved = currentActivity.packageManager.queryIntentActivities(intent, 0)
        if (resolved.isEmpty()) {
            Snackbar.make(list, R.string.error_exported_manifest, Snackbar.LENGTH_LONG)
                    .show()
        } else {
            val chooser = Intent.createChooser(intent, null)
            currentActivity.startActivity(chooser)
        }
    }

    override fun getPermissionExplanation(permission: String): Int {
        return when (permission) {
            PERMISSION_UNINSTALL -> R.string.permission_explanation_uninstall
            else -> 0
        }
    }

    override fun onPermissionGranted(requestCode: Int) {
        when (requestCode) {
            PERMISSION_REQUEST_UNINSTALL -> uninstallWithPermission()
            else -> super.onPermissionGranted(requestCode)
        }
    }

    // endregion

    // region Internal

    private fun onUninstallRequested() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (!checkPermission(PERMISSION_UNINSTALL)) {
                requestPermission(PERMISSION_UNINSTALL, PERMISSION_REQUEST_UNINSTALL)
                return
            }
        }

        uninstallWithPermission()
    }

    private fun uninstallWithPermission() {
        presenter.openUninstaller()
    }

    //endregion

    companion object {
        @TargetApi(Build.VERSION_CODES.O)
        const val PERMISSION_UNINSTALL = Manifest.permission.REQUEST_DELETE_PACKAGES

        const val PERMISSION_REQUEST_UNINSTALL = 666
    }
}
