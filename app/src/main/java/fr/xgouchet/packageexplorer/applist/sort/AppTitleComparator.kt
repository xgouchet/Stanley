package fr.xgouchet.packageexplorer.applist.sort

import fr.xgouchet.packageexplorer.applist.AppViewModel

object AppTitleComparator : Comparator<AppViewModel> {
    override fun compare(lhs: AppViewModel?, rhs: AppViewModel?): Int {
        val lhn = lhs?.title?.toLowerCase() ?: ""
        val rhn = rhs?.title?.toLowerCase() ?: ""
        return lhn.compareTo(rhn)
    }
}
