package fr.xgouchet.packageexplorer.common.sort;

import java.util.Comparator;
import java.util.Locale;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

public class PackageSortByName implements Comparator<PackageInfo> {

	private PackageManager mPackageManager;

	public PackageSortByName(final PackageManager pm) {
		mPackageManager = pm;
	}

	/**
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	@Override
	public int compare(final PackageInfo lhs, final PackageInfo rhs) {
		String lhn, rhn;

		lhn = mPackageManager.getApplicationLabel(lhs.applicationInfo)
				.toString();
		rhn = mPackageManager.getApplicationLabel(rhs.applicationInfo)
				.toString();

		if (lhn == null) {
			lhn = "";
		} else {
			lhn = lhn.toLowerCase(Locale.getDefault());
		}

		if (rhn == null) {
			rhn = "";
		} else {
			rhn = rhn.toLowerCase(Locale.getDefault());
		}

		return lhn.compareTo(rhn);
	}

}
