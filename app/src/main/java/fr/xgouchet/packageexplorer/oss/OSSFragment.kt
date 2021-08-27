package fr.xgouchet.packageexplorer.oss

import fr.xgouchet.packageexplorer.details.adapter.AppDetailsAdapter
import fr.xgouchet.packageexplorer.details.adapter.AppInfoViewModel
import fr.xgouchet.packageexplorer.ui.adapter.BaseAdapter
import fr.xgouchet.packageexplorer.ui.mvp.list.BaseListFragment

class OSSFragment :
    BaseListFragment<AppInfoViewModel, OSSPresenter>() {

    override val adapter: BaseAdapter<AppInfoViewModel> =
        AppDetailsAdapter(this, { presenter.actionTriggered(it) })

    override val isFabVisible: Boolean = false

    override val fabIconOverride: Int? = null
}
