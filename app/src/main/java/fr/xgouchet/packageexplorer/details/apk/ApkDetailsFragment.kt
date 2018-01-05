package fr.xgouchet.packageexplorer.details.apk

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.ActivityCompat
import android.support.v4.content.FileProvider
import android.support.v4.view.ViewCompat
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import fr.xgouchet.packageexplorer.BuildConfig
import fr.xgouchet.packageexplorer.R
import fr.xgouchet.packageexplorer.details.AppDetailsAdapter
import fr.xgouchet.packageexplorer.details.AppInfoViewModel
import fr.xgouchet.packageexplorer.ui.adapter.BaseAdapter
import fr.xgouchet.packageexplorer.ui.mvp.list.BaseListFragment
import java.io.File

/**
 * @author Xavier F. Gouchet
 */
class ApkDetailsFragment
    : BaseListFragment<AppInfoViewModel, ApkDetailsPresenter>() {

    companion object {
        const val PERMISSION_REQUEST_READ_STORAGE = 1
        const val PERMISSION_STORAGE = Manifest.permission.READ_EXTERNAL_STORAGE
    }

    override val adapter: BaseAdapter<AppInfoViewModel> = AppDetailsAdapter(this)
    override val isFabVisible: Boolean = false
    override val fabIconOverride: Int? = null

    // region Fragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (view != null) {
            ViewCompat.setNestedScrollingEnabled(view.findViewById(android.R.id.list), false)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            PERMISSION_REQUEST_READ_STORAGE -> handleStoragePermissionResponse(permissions, grantResults)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.apk_details, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.action_manifest -> {
                presenter.exportManifest()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    // endregion

    // region Displayer

    fun setPackageName(subtitle: String) {
        (activity as AppCompatActivity).supportActionBar?.subtitle = subtitle
    }

    fun requestStoragePermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(activity, PERMISSION_STORAGE)) {
            explainAndRequestStoragePermission()
        } else {
            doRequestStoragePermission()
        }
    }

    fun onManifestExported(dest: File) {
        val intent = Intent(Intent.ACTION_VIEW)
        val uri = FileProvider.getUriForFile(activity, BuildConfig.APPLICATION_ID, dest)
        intent.setDataAndType(uri, "text/xml")
        intent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION

        val resolved = context.packageManager.queryIntentActivities(intent, 0)
        if (resolved.isEmpty()) {
            Snackbar.make(list, R.string.error_exported_manifest, Snackbar.LENGTH_LONG)
                    .show()
        } else {
            val chooser = Intent.createChooser(intent, null)
            activity.startActivity(chooser)
        }
    }

    // endregion

    // region Internal

    private fun explainAndRequestStoragePermission() {
        Snackbar.make(list, R.string.permission_explanation_storage_apk, Snackbar.LENGTH_INDEFINITE)
                .setAction(android.R.string.ok, {
                    doRequestStoragePermission()
                })
                .show()
    }

    private fun doRequestStoragePermission() {
        requestPermissions(arrayOf(PERMISSION_STORAGE), PERMISSION_REQUEST_READ_STORAGE)
    }

    private fun handleStoragePermissionResponse(permissions: Array<out String>, grantResults: IntArray) {
        if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            presenter.onPermissionGranted()
        } else {
            Snackbar.make(list, R.string.permission_denied_storage_apk, Snackbar.LENGTH_INDEFINITE)
                    .setAction(android.R.string.ok, {
                        doRequestStoragePermission()
                    })
                    .show()
        }
    }

    // endregion
}

