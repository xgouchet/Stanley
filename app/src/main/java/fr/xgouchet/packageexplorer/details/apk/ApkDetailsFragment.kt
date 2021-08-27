package fr.xgouchet.packageexplorer.details.apk

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.core.view.ViewCompat
import com.google.android.material.snackbar.Snackbar
import fr.xgouchet.packageexplorer.BuildConfig
import fr.xgouchet.packageexplorer.R
import fr.xgouchet.packageexplorer.details.adapter.AppDetailsAdapter
import fr.xgouchet.packageexplorer.details.adapter.AppInfoViewModel
import fr.xgouchet.packageexplorer.ui.adapter.BaseAdapter
import fr.xgouchet.packageexplorer.ui.mvp.list.BaseListFragment
import java.io.File

/**
 * @author Xavier F. Gouchet
 */
class ApkDetailsFragment :
    BaseListFragment<AppInfoViewModel, ApkDetailsPresenter>() {

    override val adapter: BaseAdapter<AppInfoViewModel> =
        AppDetailsAdapter(this, { presenter.actionTriggered(it) })
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
        inflater.inflate(R.menu.apk_details, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
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
            ApkDetailsPresenter.PERMISSION_READ_STORAGE -> R.string.permission_explanation_storage_apk
            else -> 0
        }
    }

    // endregion
}
