package fr.xgouchet.packageexplorer.applist.sort

import fr.xgouchet.packageexplorer.applist.AppViewModel

/**
 * @author Xavier F. Gouchet
 */
enum class AppSort(val comparator: Comparator<AppViewModel>) {
    TITLE(AppTitleComparator),
    PACKAGE_NAME(AppPackageNameComparator),
    INSTALL_TIME(AppInstallTimeComparator),
    UPDATE_TIME(AppUpdateTimeComparator),
}
