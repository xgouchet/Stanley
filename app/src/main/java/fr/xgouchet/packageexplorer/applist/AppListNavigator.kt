package fr.xgouchet.packageexplorer.applist

import android.app.Activity
import fr.xgouchet.packageexplorer.details.app.AppDetailsActivity
import fr.xgouchet.packageexplorer.ui.mvp.Navigator
import kotlin.properties.Delegates.notNull

/**
 * @author Xavier F. Gouchet
 */
class AppListNavigator : Navigator<AppViewModel> {

    override var currentActivity: Activity by notNull()

    override fun goToItemDetails(item: AppViewModel) {
        AppDetailsActivity.startWithApp(currentActivity, item)
    }

    override fun goToItemEdition(item: AppViewModel) {
    }

    override fun goToItemCreation() {
    }

    override fun goBack() {
    }
}