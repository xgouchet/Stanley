package fr.xgouchet.packageexplorer.common;

import android.content.SharedPreferences;

public final class Settings {

	public static boolean sSimplifyNames = true;
	public static boolean sIgnoreSystemPackages = true;
	public static byte sDefaultSortMethod = Constants.SORT_BY_PACKAGE;
	public static boolean sEnableSecret = false;

	public static void updateFromPreferences(final SharedPreferences prefs) {
		sIgnoreSystemPackages = prefs.getBoolean(Constants.PREF_IGNORE_SYSTEM,
				true);
		sSimplifyNames = prefs.getBoolean(Constants.PREF_SIMPLIFY_NAMES, true);
		sEnableSecret = prefs.getBoolean(Constants.PREF_ENABLE_SECRETS, false);
	}

	public static void setEnableSecrets(final SharedPreferences prefs,
			boolean isChecked) {
		sEnableSecret = isChecked;

		prefs.edit().putBoolean(Constants.PREF_ENABLE_SECRETS, isChecked)
				.commit();

	}
}
