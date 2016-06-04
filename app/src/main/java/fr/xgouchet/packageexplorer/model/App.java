package fr.xgouchet.packageexplorer.model;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import fr.xgouchet.packageexplorer.annotations.MonitorMe;

/**
 * @author Xavier Gouchet
 */
public class App {

    @NonNull
    private final String packageName;
    @NonNull
    private final String name;
    @NonNull
    private final Drawable icon;

    private final int flags;


    public App(@NonNull String packageName,
               @NonNull String name,
               @NonNull Drawable icon,
               int flags) {

        this.packageName = packageName;
        this.name = name;
        this.icon = icon;
        this.flags = flags;
    }

    @NonNull
    public String getPackageName() {
        return packageName;
    }

    @NonNull
    public String getName() {
        return name;
    }

    @NonNull
    public Drawable getIcon() {
        return icon;
    }

    public int getFlags() {
        return flags;
    }

    @Nullable
    public static App fromPackageName(@NonNull Context context, @NonNull String packageName) {
        PackageManager pm = context.getPackageManager();
        try {
            ApplicationInfo ai = pm.getApplicationInfo(packageName, 0);
            return fromAppInfo(pm, ai);
        } catch (PackageManager.NameNotFoundException e) {
            Log.e("App", e.getMessage(), e);
            return null;
        }
    }

    @MonitorMe
    public static App fromAppInfo(@NonNull PackageManager pm, @NonNull ApplicationInfo ai) {
        return new App.Builder()
                .withPackageName(ai.packageName)
                .withName(pm.getApplicationLabel(ai).toString())
                .withIcon(pm.getApplicationIcon(ai))
                .withFlags(ai.flags)
                .build();
    }

    public static class Builder {

        @NonNull
        private String packageName = "";
        @NonNull
        private String name = "";
        @NonNull
        private Drawable icon = new ColorDrawable(Color.TRANSPARENT);
        private int flags = 0;

        public Builder() {
        }


        public Builder withPackageName(@NonNull String packageName) {
            this.packageName = packageName;
            return this;
        }

        public Builder withName(@NonNull String name) {
            this.name = name;
            return this;
        }

        public Builder withIcon(@NonNull Drawable icon) {
            this.icon = icon;
            return this;
        }

        public Builder withFlags(int flags) {
            this.flags = flags;
            return this;
        }

        public App build() {
            return new App(packageName, name, icon, flags);
        }
    }


}
