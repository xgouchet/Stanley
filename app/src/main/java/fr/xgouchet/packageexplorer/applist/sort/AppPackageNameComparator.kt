package fr.xgouchet.packageexplorer.applist.sort

import fr.xgouchet.packageexplorer.applist.AppViewModel

/**
 * @author Xavier F. Gouchet
 */
object AppPackageNameComparator : Comparator<AppViewModel> {
    override fun compare(lhs: AppViewModel?, rhs: AppViewModel?): Int {
        val lhn = lhs?.packageName?.toLowerCase() ?: ""
        val rhn = rhs?.packageName?.toLowerCase() ?: ""
        return lhn.compareTo(rhn)
    }
}
