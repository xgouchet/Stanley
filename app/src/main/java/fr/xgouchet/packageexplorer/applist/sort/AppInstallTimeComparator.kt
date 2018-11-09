package fr.xgouchet.packageexplorer.applist.sort

import fr.xgouchet.packageexplorer.applist.AppViewModel
import kotlin.math.sign

/**
 * @author Xavier F. Gouchet
 */
object AppInstallTimeComparator : Comparator<AppViewModel> {
    override fun compare(lhs: AppViewModel?, rhs: AppViewModel?): Int {
        val result: Int
        val lht = lhs?.installTime ?: 0
        val rht = rhs?.installTime ?: 0
        result = if (lht == rht) {
            val lhn = lhs?.packageName?.toLowerCase() ?: ""
            val rhn = rhs?.packageName?.toLowerCase() ?: ""
            lhn.compareTo(rhn)
        } else {
            (rht - lht).sign
        }
        return result
    }
}