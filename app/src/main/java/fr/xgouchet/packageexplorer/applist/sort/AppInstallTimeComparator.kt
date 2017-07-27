package fr.xgouchet.packageexplorer.applist.sort

import fr.xgouchet.packageexplorer.applist.AppViewModel

/**
 * @author Xavier F. Gouchet
 */
object AppInstallTimeComparator : Comparator<AppViewModel> {
    override fun compare(lhs: AppViewModel?, rhs: AppViewModel?): Int {
        val lht = lhs?.installTime ?: 0
        val rht = rhs?.installTime ?: 0
        return (rht - lht).toInt()
    }
}