package fr.xgouchet.packageexplorer.core.mvp

import android.app.Activity
import android.view.View

/**
 * @author Xavier F. Gouchet
 */
interface Navigator<in T> {

    fun goToItemDetails(item: T,
                        activity: Activity,
                        transitionView: View? = null)

    fun goToItemEdition(item: T)

    fun goToItemCreation()

    fun goBack()
}

