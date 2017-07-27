package fr.xgouchet.packageexplorer.appdetails

import fr.xgouchet.packageexplorer.core.mvp.BaseDiffUtilCallback

/**
 * @author Xavier F. Gouchet
 */
class AppInfoDiffUtilCallback(oldContent: List<AppInfoViewModel>,
                              newContent: List<AppInfoViewModel>)
    : BaseDiffUtilCallback<AppInfoViewModel>(oldContent, newContent) {

    override fun areItemsRepresentingTheSameObject(oldItem: AppInfoViewModel, newItem: AppInfoViewModel): Boolean {
        return oldItem == newItem
    }

    override fun areItemContentsTheSame(oldItem: AppInfoViewModel, newItem: AppInfoViewModel): Boolean {
        return oldItem == newItem
    }
}