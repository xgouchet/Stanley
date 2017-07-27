package fr.xgouchet.packageexplorer.applist

import fr.xgouchet.packageexplorer.core.mvp.BaseDiffUtilCallback

class AppDiffUtilCallback(oldContent: List<AppViewModel>,
                          newContent: List<AppViewModel>)
    : BaseDiffUtilCallback<AppViewModel>(oldContent, newContent) {

    override fun areItemsRepresentingTheSameObject(oldItem: AppViewModel, newItem: AppViewModel): Boolean {
        return oldItem == newItem
    }

    override fun areItemContentsTheSame(oldItem: AppViewModel, newItem: AppViewModel): Boolean {
        return oldItem.packageName.toLowerCase() == newItem.packageName.toLowerCase()
    }

}