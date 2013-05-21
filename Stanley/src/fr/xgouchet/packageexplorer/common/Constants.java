package fr.xgouchet.packageexplorer.common;

import java.util.Comparator;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import fr.xgouchet.packageexplorer.common.sort.PackageSortByInstall;
import fr.xgouchet.packageexplorer.common.sort.PackageSortByName;
import fr.xgouchet.packageexplorer.common.sort.PackageSortByPackage;
import fr.xgouchet.packageexplorer.common.sort.PackageSortByUpdate;

public class Constants {

	public static final String TAG_FRAGMENT_DETAILS = "details";
	public static final String TAG_FRAGMENT_RESOURCES = "resources";
	public static final String TAG_FRAGMENT_LIST = "list";
	public static final String TAG_FRAGMENT_RESOLVE = "resolve";

	public static final String PREFERENCES = "fr.xgouchet.packageexplorer";

	public static final String PREF_IGNORE_SYSTEM = "ignore_system_packages";
	public static final String PREF_SIMPLIFY_NAMES = "simplify_names";
	public static final String PREF_ENABLE_SECRETS = "enable_secrets";

	public static final String EXTRA_PACKAGE_INFO = "package_info";
	public static final String EXTRA_RESOLVE_INFO = "resolve_info";
	public static final String EXTRA_RESOURCES_DIR = "resources_dir";

	public static final byte SORT_BY_NAME = 0;
	public static final byte SORT_BY_PACKAGE = 1;
	public static final byte SORT_BY_INSTALL = 2;
	public static final byte SORT_BY_UPDATE = 3;

	private Constants() {

	}

	public static final Comparator<PackageInfo> getComparator(
			final PackageManager pm, final int mSortMethod) {
		Comparator<PackageInfo> comp = null;

		switch (mSortMethod) {
		case SORT_BY_PACKAGE:
			comp = new PackageSortByPackage();
			break;
		case SORT_BY_INSTALL:
			comp = new PackageSortByInstall();
			break;
		case SORT_BY_UPDATE:
			comp = new PackageSortByUpdate();
			break;
		case SORT_BY_NAME:
		default:
			comp = new PackageSortByName(pm);
			break;
		}

		return comp;
	}

}
