package fr.xgouchet.packageexplorer.applist

import fr.xgouchet.packageexplorer.ui.adapter.BaseDiffUtilCallback

class AppDiffUtilCallback(
    oldContent: List<AppViewModel>,
    newContent: List<AppViewModel>
) :
    BaseDiffUtilCallback<AppViewModel>(oldContent, newContent) {

    override fun areItemsRepresentingTheSameObject(oldItem: AppViewModel, newItem: AppViewModel): Boolean {
        return oldItem.packageName.toLowerCase() == newItem.packageName.toLowerCase()
    }

    override fun areItemContentsTheSame(oldItem: AppViewModel, newItem: AppViewModel): Boolean {
        return oldItem == newItem
    }
}
