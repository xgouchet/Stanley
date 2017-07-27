package fr.xgouchet.packageexplorer.applist.sort

import fr.xgouchet.packageexplorer.applist.AppViewModel

/**
 * @author Xavier F. Gouchet
 */
object AppUpdateTimeComparator : Comparator<AppViewModel> {
    override fun compare(lhs: AppViewModel?, rhs: AppViewModel?): Int {
        val lht = lhs?.updateTime ?: 0
        val rht = rhs?.updateTime ?: 0
        return (rht - lht).toInt()
    }
}