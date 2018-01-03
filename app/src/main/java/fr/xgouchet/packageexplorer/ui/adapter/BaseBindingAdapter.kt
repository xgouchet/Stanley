package fr.xgouchet.packageexplorer.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup

abstract class BaseBindingAdapter<T, B>
    : BaseAdapter<T>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<T> {

        val layoutInflater = LayoutInflater.from(parent.context)
        val binding: B = inflateDataBinding(layoutInflater, parent, viewType)
        return instantiateViewHolder(binding, viewType)
    }

    abstract fun inflateDataBinding(layoutInflater: LayoutInflater, parent: ViewGroup, viewType: Int): B

    protected abstract fun instantiateViewHolder(binding: B, viewType: Int): BaseViewHolder<T>
}