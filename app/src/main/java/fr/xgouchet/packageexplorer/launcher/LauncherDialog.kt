package fr.xgouchet.packageexplorer.launcher

import android.app.Dialog
import android.content.pm.ResolveInfo
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import fr.xgouchet.packageexplorer.R
import fr.xgouchet.packageexplorer.core.utils.getResolvedIntent
import io.reactivex.rxjava3.functions.BiConsumer
import java.util.Optional

class LauncherDialog :
    DialogFragment(),
    BiConsumer<ResolveInfo, Optional<View>> {

    companion object {
        const val KEY_DATA = "data_resolve_info"

        @JvmStatic
        fun withData(data: List<ResolveInfo>): LauncherDialog {
            val dialog = LauncherDialog()
            val bundle = Bundle(1)
            bundle.putParcelableArray(KEY_DATA, data.toTypedArray())
            dialog.arguments = bundle
            return dialog
        }
    }

    private lateinit var data: List<ResolveInfo>
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ResolveInfoAdapter

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val currentActivity = requireActivity()
        recyclerView = RecyclerView(currentActivity)

        @Suppress("UNCHECKED_CAST")
        data = arguments?.getParcelableArray(KEY_DATA)?.toList() as List<ResolveInfo>? ?: emptyList()
        adapter = ResolveInfoAdapter(data, this, currentActivity.packageManager)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(currentActivity)
        // TODO add listener

        return AlertDialog.Builder(currentActivity)
            .setTitle(R.string.dialog_title_launcher)
            .setView(recyclerView)
            .create()
    }

    override fun accept(info: ResolveInfo, view: Optional<View>) {
        dismiss()
        startActivity(getResolvedIntent(info))
    }
}
