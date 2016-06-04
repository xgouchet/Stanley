package fr.xgouchet.packageexplorer.model;

import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;

/**
 * @author Xavier Gouchet
 */
public class AppInfo {
    public static final int INFO_TYPE_GLOBAL = 0;
    public static final int INFO_TYPE_FEATURES_REQUIRED = 1;
    public static final int INFO_TYPE_CUSTOM_PERMISSIONS = 2;
    public static final int INFO_TYPE_PERMISSIONS = 3;
    public static final int INFO_TYPE_ACTIVITIES = 4;
    public static final int INFO_TYPE_SERVICES = 5;
    public static final int INFO_TYPE_PROVIDERS = 6;
    public static final int INFO_TYPE_RECEIVERS = 7;

    public static final int INFO_TYPES_COUNT = 8;

    private final int type;
    @NonNull
    private final String info;

    private final boolean header;
    @StringRes
    private final int infoRes;
    @Nullable
    private final String subInfo;
    @Nullable
    private final Drawable icon;

    public AppInfo(int type, @NonNull String info) {
        this(type, info, false);
    }

    public AppInfo(int type, @NonNull String info, boolean header) {
        this.type = type;
        this.info = info;
        this.header = header;
        this.infoRes = 0;
        subInfo = null;
        icon = null;
    }

    public AppInfo(int type, @StringRes int infoRes, boolean header) {
        this.type = type;
        this.info = "";
        this.header = header;
        this.infoRes = infoRes;
        subInfo = null;
        icon = null;
    }

    public AppInfo(int type, @NonNull String info, @Nullable String subInfo, @Nullable Drawable icon) {
        this.type = type;
        this.info = info;
        this.header = false;
        this.infoRes = 0;
        this.subInfo = subInfo;
        this.icon = icon;

    }

    public AppInfo(int type, @NonNull String info, @Nullable String subInfo) {
        this.type = type;
        this.info = info;
        this.subInfo = subInfo;
        this.header = false;
        this.infoRes = 0;
        this.icon = null;
    }

    public int getType() {
        return type;
    }

    @NonNull
    public String getInfo() {
        return info;
    }

    @Nullable
    public String getSubInfo() {
        return subInfo;
    }

    @Nullable
    public Drawable getIcon() {
        return icon;
    }

    @StringRes
    public int getInfoRes() {
        return infoRes;
    }

    public boolean isHeader() {
        return header;
    }


}
