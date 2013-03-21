package fr.xgouchet.packageexplorer.common;

import android.content.SharedPreferences;

public final class Settings {

	public static boolean sSimplifyNames = true;
	
	public static boolean sIgnoreSystemPacakges = true;

	public static byte sDefaultSortMethod = Constants.SORT_BY_PACKAGE;
	public static byte sSortMethod = sDefaultSortMethod;

	public static void updateFromPreferences(SharedPreferences sharedPreferences) {
		// TODO Auto-generated method stub

	}

}
