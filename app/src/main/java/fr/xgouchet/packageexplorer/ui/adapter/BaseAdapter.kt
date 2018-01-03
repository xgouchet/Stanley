package fr.xgouchet.packageexplorer.ui.adapter

import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
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

