package fr.xgouchet.packageexplorer.core.mvp

import android.support.annotation.LayoutRes
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import java.util.*


/**
 * @author Xavier Gouchet
 */
abstract class BaseAdapter< T>
    : RecyclerView.Adapter<BaseViewHolder<T>>() {


    protected var content: List<T> = ArrayList()

    override fun onBindViewHolder(holder: BaseViewHolder<T>, position: Int) {
        val item = content[position]
        holder.bindItem(item)
    }

    override fun getItemCount(): Int {
        return content.size
    }

    fun getItem(position: Int): T {
        return content[position]
    }

    fun update(newContent: List<T>) {
        val helper = getDiffHelper(content, newContent)

        if (helper != null) {
            val result = DiffUtil.calculateDiff(getDiffHelper(content, newContent))
            result.dispatchUpdatesTo(this)
            content = newContent
        } else {
            content = newContent
            notifyDataSetChanged()
        }
    }

    abstract fun getDiffHelper(oldContent: List<T>, newContent: List<T>): DiffUtil.Callback?
}

abstract class BaseLayoutAdapter<T>
    : BaseAdapter<T>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<T> {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(getLayoutId(viewType), parent, false)
        return instantiateViewHolder(view, viewType)
    }

    @LayoutRes abstract fun getLayoutId(viewType: Int): Int

    protected abstract fun instantiateViewHolder(view: View, viewType: Int): BaseViewHolder<T>
}

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