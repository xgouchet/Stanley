package fr.xgouchet.packageexplorer.core;

import android.content.Context;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.text.TextUtils;

/**
 * @author Xavier Gouchet
 */
public final class Preferences {

    public static final String KEY_SORT_CRITERIA = "sort_criteria";
    public static final String KEY_DISPLAY_SYSTEM_APP = "show_system_apps";

    public static final String SORT_NAME = "name";
    public static final String SORT_PACKAGE_NAME = "package_name";
    public static final String SORT_INSTALL_TIME = "install_time";

    private static String lastSort = "";
    private static boolean lastDisplay;

    public static boolean getDisplaySystemApp(@NonNull Context context) {
        lastDisplay = PreferenceManager.getDefaultSharedPreferences(context)
                .getBoolean(KEY_DISPLAY_SYSTEM_APP, false);
        return lastDisplay;
    }

    public static boolean sortOrDisplayChanged(@NonNull Context context) {
        boolean oldDisplay = lastDisplay;
        boolean newDisplay = getDisplaySystemApp(context);
        if (oldDisplay != newDisplay) return true;

        String oldSort = lastSort;
        String newSort = getSort(context);
        if (!TextUtils.equals(newSort, oldSort)) return true;

        return false;
    }

    @NonNull
    public static String getSort(@NonNull Context context) {
        lastSort = PreferenceManager.getDefaultSharedPreferences(context)
                .getString(KEY_SORT_CRITERIA, SORT_NAME);
        return lastSort;
    }

    private Preferences() {
    }
}
