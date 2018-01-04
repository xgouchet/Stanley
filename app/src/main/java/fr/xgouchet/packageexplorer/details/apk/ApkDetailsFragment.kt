package fr.xgouchet.packageexplorer.details.apk

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.ActivityCompat
import android.support.v4.view.ViewCompat
import android.support.v7.app.AppCompatActivity
import android.view.View
import fr.xgouchet.packageexplorer.R
import fr.xgouchet.packageexplorer.details.AppDetailsAdapter
import fr.xgouchet.packageexplorer.details.AppInfoViewModel
import fr.xgouchet.packageexplorer.ui.adapter.BaseAdapter
import fr.xgouchet.packageexplorer.ui.mvp.list.BaseListFragment

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

