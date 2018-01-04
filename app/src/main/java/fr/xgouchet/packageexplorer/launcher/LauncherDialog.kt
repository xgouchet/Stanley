package fr.xgouchet.packageexplorer.launcher

import android.app.Dialog
import android.content.pm.ResolveInfo
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import fr.xgouchet.packageexplorer.R
import fr.xgouchet.packageexplorer.core.utils.getResolvedIntent
import io.reactivex.functions.BiConsumer


class LauncherDialog
    : DialogFragment(),
        BiConsumer<ResolveInfo, View?> {

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
        recyclerView = RecyclerView(context)

        @Suppress("UNCHECKED_CAST")
        data = arguments?.getParcelableArray(KEY_DATA)?.toList() as List<ResolveInfo>? ?: emptyList()
        adapter = ResolveInfoAdapter(data, this, context.packageManager)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(context)
        // TODO add listener

        return AlertDialog.Builder(context)
                .setTitle(R.string.dialog_title_launcher)
                .setView(recyclerView)
                .create()
    }

    override fun accept(info: ResolveInfo, view: View?) {
        dismiss()
        startActivity(getResolvedIntent(info))
    }

}