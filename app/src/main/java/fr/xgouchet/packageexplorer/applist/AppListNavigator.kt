package fr.xgouchet.packageexplorer.applist

import android.app.Activity
import android.view.View
import fr.xgouchet.packageexplorer.details.app.AppDetailsActivity
import fr.xgouchet.packageexplorer.core.mvp.Navigator

/**
 * @author Xavier F. Gouchet
 */
class AppListNavigator : Navigator<AppViewModel> {

    override fun goToItemDetails(item: AppViewModel,
                                 activity: Activity,
                                 transitionView: View?) {
        AppDetailsActivity.startWithAppAndMaybeTransition(activity, item, transitionView)
    }

    override fun goToItemEdition(item: AppViewModel) {
    }

    override fun goToItemCreation() {
    }

    override fun goBack() {
    }
}