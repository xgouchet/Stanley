package fr.xgouchet.packageexplorer.common.sort;

import java.util.Comparator;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

public class PackageSortByName implements Comparator<PackageInfo> {

	private PackageManager mPackageManager;

	public PackageSortByName(PackageManager pm) {
		mPackageManager = pm;
	}

	/**
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	public int compare(PackageInfo lhs, PackageInfo rhs) {
		String lhn, rhn;

		lhn = mPackageManager.getApplicationLabel(lhs.applicationInfo)
				.toString();
		rhn = mPackageManager.getApplicationLabel(rhs.applicationInfo)
				.toString();

		if (lhn == null) {
			lhn = "";
		}

		return lhn.compareTo(rhn);
	}

}
