package fr.xgouchet.packageexplorer.ui.adapter

import androidx.recyclerview.widget.DiffUtil

/**
 * @author Xavier F. Gouchet
 */
abstract class BaseDiffUtilCallback<T>(
    val oldContent: List<T>,
    val newContent: List<T>
) :
    DiffUtil.Callback() {
    override fun getOldListSize(): Int {
        return oldContent.size
    }

    override fun getNewListSize(): Int {
        return newContent.size
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldContent[oldItemPosition]
        val newItem = newContent[newItemPosition]

        return areItemContentsTheSame(oldItem, newItem)
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldContent[oldItemPosition]
        val newItem = newContent[newItemPosition]

        return areItemsRepresentingTheSameObject(oldItem, newItem)
    }

    abstract fun areItemsRepresentingTheSameObject(oldItem: T, newItem: T): Boolean

    abstract fun areItemContentsTheSame(oldItem: T, newItem: T): Boolean
}
