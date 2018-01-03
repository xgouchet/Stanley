package fr.xgouchet.packageexplorer.details.apk

import android.os.Bundle
import android.support.v4.view.ViewCompat
import android.support.v7.app.AppCompatActivity
import android.view.View
import fr.xgouchet.packageexplorer.core.mvp.BaseAdapter
import fr.xgouchet.packageexplorer.core.mvp.ListFragment
import fr.xgouchet.packageexplorer.details.AppDetailsAdapter
import fr.xgouchet.packageexplorer.details.AppInfoViewModel

/**
 * @author Xavier F. Gouchet
 */
class ApkDetailsFragment
    : ListFragment<AppInfoViewModel, ApkDetailsPresenter>(false) {

    override val adapter: BaseAdapter<AppInfoViewModel> = AppDetailsAdapter(this)

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (view != null) {
            ViewCompat.setNestedScrollingEnabled(view.findViewById(android.R.id.list), false)
        }
    }

    fun setPackageName(subtitle: String) {
        (activity as AppCompatActivity).supportActionBar?.subtitle = subtitle
    }

}

