package fr.xgouchet.packageexplorer.ui.mvp

import android.app.Activity

/**
 * @author Xavier F. Gouchet
 */
interface Navigator<in T> {

    var currentActivity: Activity

    fun goToItemDetails(item: T)

    fun goToItemEdition(item: T)

    fun goToItemCreation()

    fun goBack()
}

