package fr.xgouchet.packageexplorer.appdetails

import android.os.Bundle
import android.support.v4.view.ViewCompat
import android.view.View
import fr.xgouchet.packageexplorer.core.mvp.BaseAdapter
import fr.xgouchet.packageexplorer.core.mvp.ListFragment

/**
 * @author Xavier F. Gouchet
 */
class AppDetailsFragment
    : ListFragment<AppInfoViewModel, AppDetailsPresenter>(false) {

    override val adapter: BaseAdapter<AppInfoViewModel> = AppDetailsAdapter(this)

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (view != null) {
            ViewCompat.setNestedScrollingEnabled(view.findViewById(android.R.id.list), false)
        }
    }
}

