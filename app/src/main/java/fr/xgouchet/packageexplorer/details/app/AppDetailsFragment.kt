package fr.xgouchet.packageexplorer.details.app

import android.os.Bundle
import android.support.v4.view.ViewCompat
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import fr.xgouchet.packageexplorer.R
import fr.xgouchet.packageexplorer.ui.adapter.BaseAdapter
import fr.xgouchet.packageexplorer.ui.mvp.list.BaseListFragment
import fr.xgouchet.packageexplorer.details.AppDetailsAdapter
import fr.xgouchet.packageexplorer.details.AppInfoViewModel

/**
 * @author Xavier F. Gouchet
 */
class AppDetailsFragment
    : BaseListFragment<AppInfoViewModel, AppDetailsPresenter>(false) {

    override val adapter: BaseAdapter<AppInfoViewModel> = AppDetailsAdapter(this)

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

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.app_details, menu)

        if (presenter.isSystemApp){
            menu?.findItem(R.id.action_uninstall)?.isVisible = false
            menu?.findItem(R.id.action_play_store)?.isVisible = false
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.action_app_info -> {
                presenter.openAppInfo()
                return true
            }
            R.id.action_play_store -> {
                presenter.openPlayStore()
                return true
            }
            R.id.action_uninstall-> {
                presenter.openUninstaller()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}

