package fr.xgouchet.packageexplorer.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes

abstract class BaseLayoutAdapter<T : Any> :
    BaseAdapter<T>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<T> {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(getLayoutId(viewType), parent, false)
        return instantiateViewHolder(view, viewType)
    }

    @LayoutRes
    abstract fun getLayoutId(viewType: Int): Int

    protected abstract fun instantiateViewHolder(view: View, viewType: Int): BaseViewHolder<T>
}
