package fr.xgouchet.packageexplorer.common.sort;

import java.util.Comparator;

import android.content.pm.PackageInfo;

public class PackageSortByUpdate implements Comparator<PackageInfo> {

	public PackageSortByUpdate() {
	}

	/**
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	public int compare(PackageInfo lhs, PackageInfo rhs) {

		return (int) Math.signum(rhs.firstInstallTime - lhs.firstInstallTime);
	}

}
