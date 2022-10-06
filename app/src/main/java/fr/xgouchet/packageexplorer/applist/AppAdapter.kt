package fr.xgouchet.packageexplorer.applist

import android.view.View
import androidx.recyclerview.widget.DiffUtil
import fr.xgouchet.packageexplorer.R
import fr.xgouchet.packageexplorer.ui.adapter.BaseLayoutAdapter
import fr.xgouchet.packageexplorer.ui.adapter.BaseViewHolder
import io.reactivex.rxjava3.functions.BiConsumer
import io.reactivex.rxjava3.functions.Consumer
import java.util.Optional

/**
 * @author Xavier F. Gouchet
 */
class AppAdapter(
    val listener: BiConsumer<AppViewModel, Optional<View>>?,
    val actionListener: Consumer<AppViewModel>?
) : BaseLayoutAdapter<AppViewModel>() {

    override fun getLayoutId(viewType: Int): Int = R.layout.item_app

    override fun instantiateViewHolder(view: View, viewType: Int): BaseViewHolder<AppViewModel> {
        return AppViewHolder(view, listener, actionListener)
    }

    override fun getDiffHelper(oldContent: List<AppViewModel>, newContent: List<AppViewModel>): DiffUtil.Callback? {
        return AppDiffUtilCallback(oldContent, newContent)
    }
}
