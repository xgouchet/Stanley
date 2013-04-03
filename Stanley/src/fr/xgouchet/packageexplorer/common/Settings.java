package fr.xgouchet.packageexplorer.common;

import android.content.SharedPreferences;

public final class Settings {

	public static boolean sSimplifyNames = true;
	public static boolean sIgnoreSystemPackages = true;
	public static byte sDefaultSortMethod = Constants.SORT_BY_PACKAGE;

	public static void updateFromPreferences(final SharedPreferences prefs) {
		sIgnoreSystemPackages = prefs.getBoolean(Constants.PREF_IGNORE_SYSTEM,
				true);
		sSimplifyNames = prefs.getBoolean(Constants.PREF_SIMPLIFY_NAMES, true);
	}
}
