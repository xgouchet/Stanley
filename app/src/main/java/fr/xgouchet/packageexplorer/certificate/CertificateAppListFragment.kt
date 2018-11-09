package fr.xgouchet.packageexplorer.certificate

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import fr.xgouchet.packageexplorer.R
import fr.xgouchet.packageexplorer.applist.AppAdapter
import fr.xgouchet.packageexplorer.applist.AppViewModel
import fr.xgouchet.packageexplorer.applist.sort.AppSort
import fr.xgouchet.packageexplorer.ui.adapter.BaseAdapter
import fr.xgouchet.packageexplorer.ui.mvp.list.BaseListFragment


class CertificateAppListFragment : BaseListFragment<AppViewModel, CertificateAppListPresenter>() {

    override val adapter: BaseAdapter<AppViewModel> = AppAdapter(this)
    override val isFabVisible: Boolean = false
    override val fabIconOverride: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.certificate_app_list, menu)


        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        when (item?.itemId) {
            R.id.sort_by_title -> presenter.setSort(AppSort.TITLE)
            R.id.sort_by_package_name -> presenter.setSort(AppSort.PACKAGE_NAME)
            R.id.sort_by_install_time -> presenter.setSort(AppSort.INSTALL_TIME)
            R.id.sort_by_update_time -> presenter.setSort(AppSort.UPDATE_TIME)
            else -> return super.onOptionsItemSelected(item)
        }
        return true
    }
}