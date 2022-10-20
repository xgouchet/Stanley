package fr.xgouchet.packageexplorer.ui.mvp

import android.app.Activity

interface Navigator<in T> {

    var currentActivity: Activity

    fun goToItemDetails(item: T) {}

    fun goToItemEdition(item: T) {}

    fun goToItemCreation() {}

    fun goBack() {}
}
