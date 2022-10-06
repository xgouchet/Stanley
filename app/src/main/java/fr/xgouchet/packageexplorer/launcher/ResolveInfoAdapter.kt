package fr.xgouchet.packageexplorer.launcher

import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.view.View
import androidx.recyclerview.widget.DiffUtil
import fr.xgouchet.packageexplorer.R
import fr.xgouchet.packageexplorer.ui.adapter.BaseLayoutAdapter
import fr.xgouchet.packageexplorer.ui.adapter.BaseViewHolder
import io.reactivex.rxjava3.functions.BiConsumer
import java.util.Optional

class ResolveInfoAdapter(
    data: List<ResolveInfo>,
    val listener: BiConsumer<ResolveInfo, Optional<View>>,
    val pm: PackageManager
) :
    BaseLayoutAdapter<ResolveInfo>() {

    init {
        content = data
    }

    override fun getLayoutId(viewType: Int): Int {
        return R.layout.item_launcher_info
    }

    override fun instantiateViewHolder(view: View, viewType: Int): BaseViewHolder<ResolveInfo> {
        return ResolveInfoViewHolder(view, listener, pm)
    }

    override fun getDiffHelper(
        oldContent: List<ResolveInfo>,
        newContent: List<ResolveInfo>
    ): DiffUtil.Callback? {
        return null
    }
}
