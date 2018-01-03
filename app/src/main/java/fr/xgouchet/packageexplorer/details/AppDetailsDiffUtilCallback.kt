package fr.xgouchet.packageexplorer.details

import fr.xgouchet.packageexplorer.ui.adapter.BaseDiffUtilCallback

/**
 * @author Xavier F. Gouchet
 */
class AppDetailsDiffUtilCallback(oldContent: List<AppInfoViewModel>,
                                 newContent: List<AppInfoViewModel>)
    : BaseDiffUtilCallback<AppInfoViewModel>(oldContent, newContent) {

    override fun areItemsRepresentingTheSameObject(oldItem: AppInfoViewModel, newItem: AppInfoViewModel): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areItemContentsTheSame(oldItem: AppInfoViewModel, newItem: AppInfoViewModel): Boolean {
        return oldItem == newItem
    }
}