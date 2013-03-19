package fr.xgouchet.packageexplorer.common.sort;

import java.util.Comparator;

import android.content.pm.PackageInfo;

public class PackageSortByPackage implements Comparator<PackageInfo> {

	public PackageSortByPackage() {
	}

	/**
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	public int compare(PackageInfo lhs, PackageInfo rhs) {
		String lhn, rhn;

		lhn = lhs.packageName.toLowerCase();
		rhn = rhs.packageName.toLowerCase();

		if (lhn == null) {
			lhn = "";
		}

		return lhn.compareTo(rhn);
	}

}
