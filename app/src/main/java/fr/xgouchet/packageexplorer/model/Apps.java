package fr.xgouchet.packageexplorer.model;

import android.content.ComponentName;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.FeatureInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PermissionInfo;
import android.content.pm.ProviderInfo;
import android.content.pm.ServiceInfo;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.util.Log;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.jar.Manifest;

import fr.xgouchet.packageexplorer.R;
import fr.xgouchet.packageexplorer.annotations.MonitorMe;
import rx.Observable;
import rx.Subscriber;

/**
 * @author Xavier Gouchet
 */
public class Apps {
    private static final DateFormat DATE_FORMAT = DateFormat
            .getDateInstance(DateFormat.LONG);

    public static class List implements Observable.OnSubscribe<App> {


        @NonNull
        private final Context context;

        public List(@NonNull Context context) {
            this.context = context;
        }

        @Override
        public void call(Subscriber<? super App> subscriber) {
            PackageManager pm = context.getPackageManager();
            java.util.List<PackageInfo> packages = pm.getInstalledPackages(0);

            ApplicationInfo ai;
            for (PackageInfo pi : packages) {
                ai = pi.applicationInfo;
                if (ai == null) continue;

                App app = App.fromAppInfo(pm, ai);

                subscriber.onNext(app);
            }

            subscriber.onCompleted();
        }
    }

    public static class Infos implements Observable.OnSubscribe<AppInfo> {

        public static final int FULL_INFO_FLAGS = PackageManager.GET_ACTIVITIES
                | PackageManager.GET_GIDS
                | PackageManager.GET_CONFIGURATIONS
                | PackageManager.GET_INSTRUMENTATION
                | PackageManager.GET_PERMISSIONS
                | PackageManager.GET_PROVIDERS
                | PackageManager.GET_RECEIVERS
                | PackageManager.GET_SERVICES
                | PackageManager.GET_SIGNATURES;

        @NonNull
        private final Context context;
        @NonNull
        private final String packageName;

        public Infos(@NonNull Context context, @NonNull String packageName) {
            this.context = context;
            this.packageName = packageName;
        }

        @Override
        public void call(Subscriber<? super AppInfo> subscriber) {

            PackageManager pm = context.getPackageManager();
            PackageInfo pi;
            ApplicationInfo ai;
            try {
                pi = pm.getPackageInfo(packageName, FULL_INFO_FLAGS);
                ai = pm.getApplicationInfo(packageName, 0);
            } catch (PackageManager.NameNotFoundException e) {
                subscriber.onError(e);
                return;
            }

            globalInfos(subscriber, pi, ai);
            features(subscriber, pi);
            customPermissions(subscriber, pi);
            permissions(subscriber, pi);
            activities(subscriber, pm, pi);
            services(subscriber, pi);
            providers(subscriber, pi);
            receivers(subscriber, pi);

            subscriber.onCompleted();
        }

        @MonitorMe
        private void globalInfos(@NonNull Subscriber<? super AppInfo> subscriber, @NonNull PackageInfo pi, @NonNull ApplicationInfo ai) {

//            subscriber.onNext(new AppInfo(AppInfo.INFO_TYPE_GLOBAL, pi.packageName, false));
            subscriber.onNext(new AppInfo(AppInfo.INFO_TYPE_GLOBAL, "Version Code : " + pi.versionCode, false));
            subscriber.onNext(new AppInfo(AppInfo.INFO_TYPE_GLOBAL, "Version Name : \"" + pi.versionName + "\"", false));
            subscriber.onNext(new AppInfo(AppInfo.INFO_TYPE_GLOBAL, "Installed  : " + DATE_FORMAT.format(new Date(pi.firstInstallTime)), false));
            subscriber.onNext(new AppInfo(AppInfo.INFO_TYPE_GLOBAL, "Updated  : " + DATE_FORMAT.format(new Date(pi.lastUpdateTime)), false));
            subscriber.onNext(new AppInfo(AppInfo.INFO_TYPE_GLOBAL, "Target SDK : " + ((ai.targetSdkVersion > 0) ? ai.targetSdkVersion : "?"), false));

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                String installLocation = null;
                switch (pi.installLocation) {
                    case PackageInfo.INSTALL_LOCATION_AUTO:
                        installLocation = "Install Location : Auto";
                        break;
                    case PackageInfo.INSTALL_LOCATION_INTERNAL_ONLY:
                        installLocation = "Install Location : Internal";
                        break;
                    case PackageInfo.INSTALL_LOCATION_PREFER_EXTERNAL:
                        installLocation = "Install Location : External (if possible)";
                        break;
                }
                if (installLocation != null) {
                    subscriber.onNext(new AppInfo(AppInfo.INFO_TYPE_GLOBAL, installLocation, false));
                }
            }
        }

        @MonitorMe
        private void features(@NonNull Subscriber<? super AppInfo> subscriber, @NonNull PackageInfo pi) {
            if (pi.reqFeatures != null) {
                for (FeatureInfo fi : pi.reqFeatures) {
                    String info;
                    if (fi.name == null) {
                        int maj, min;
                        maj = fi.reqGlEsVersion >> 16;
                        min = fi.reqGlEsVersion & 0xFFFF;
                        info = String.format(Locale.US, "OpenGL ES v%d.%d", maj, min);
                    } else {
                        int stringRes = context.getResources().getIdentifier(fi.name, "string", context.getPackageName());
                        if (stringRes == 0) {
                            Log.e("Apps", "Unable to find description for <\"" + fi.name + "\">");
                            info = fi.name;
                        } else {
                            info = context.getString(stringRes);
                        }
                    }
                    if (fi.flags == FeatureInfo.FLAG_REQUIRED) info += " (REQUIRED)";
                    subscriber.onNext(new AppInfo(AppInfo.INFO_TYPE_FEATURES_REQUIRED, info));
                }
            }
        }

        @MonitorMe
        private void customPermissions(@NonNull Subscriber<? super AppInfo> subscriber, @NonNull PackageInfo pi) {
            if (pi.permissions != null) {
                for (PermissionInfo cpi : pi.permissions) {
                    String info;
                    if (cpi.name.endsWith(".C2D_MESSAGE")) {
                        info = context.getString(R.string.c2d_message_generic);
                    } else {
                        info = simplifyName(cpi.name, pi.packageName);
                    }
                    subscriber.onNext(new AppInfo(AppInfo.INFO_TYPE_CUSTOM_PERMISSIONS, info));
                }
            }
        }

        @MonitorMe
        private void permissions(@NonNull Subscriber<? super AppInfo> subscriber, @NonNull PackageInfo pi) {
            if (pi.requestedPermissions != null) {
                for (String name : pi.requestedPermissions) {
                    String info, subinfo;
                    int stringRes = context.getResources().getIdentifier(name, "string", context.getPackageName());
                    if (stringRes == 0) {
                        if (name.endsWith(".permission.C2D_MESSAGE")) {
                            info = context.getString(R.string.c2d_message_generic);
                            subinfo = ".permission.C2D_MESSAGE";
                        } else {
                            Log.e("Apps", "Unable to find description for <\"" + name + "\">");
                            info = null;
                            if (name.startsWith("android.permission.")) {
                                subinfo = name.substring("android.permission.".length());
                            } else {
                                subinfo = simplifyName(name, pi.packageName);
                            }
                        }
                    } else {
                        if (name.startsWith("android.permission.")) {
                            subinfo = name.substring("android.permission.".length());
                        } else {
                            subinfo = name;
                        }
                        info = context.getString(stringRes);
                    }
                    subscriber.onNext(new AppInfo(AppInfo.INFO_TYPE_PERMISSIONS, subinfo, info));
                }
            }
        }

        @MonitorMe
        private void activities(@NonNull Subscriber<? super AppInfo> subscriber, @NonNull PackageManager pm, @NonNull PackageInfo pi) {
            if (pi.activities != null) {
                for (ActivityInfo ai : pi.activities) {
                    String name = simplifyName(ai.name, pi.packageName);
                    String label = ai.loadLabel(pm).toString();
                    ComponentName component = new ComponentName(pi.packageName, ai.name);
                    Drawable icon = null;
                    try {
                        icon = pm.getActivityIcon(component);
                    } catch (PackageManager.NameNotFoundException ignore) {
                    }

                    subscriber.onNext(new AppInfo(AppInfo.INFO_TYPE_ACTIVITIES, label, name, icon));
                }
            }
        }

        @MonitorMe
        private void services(@NonNull Subscriber<? super AppInfo> subscriber, @NonNull PackageInfo pi) {
            if (pi.services != null) {
                for (ServiceInfo si : pi.services) {
                    subscriber.onNext(
                            new AppInfo(AppInfo.INFO_TYPE_SERVICES, simplifyName(si.name, pi.packageName)));
                }
            }
        }

        @MonitorMe
        private void providers(@NonNull Subscriber<? super AppInfo> subscriber, @NonNull PackageInfo pi) {
            if (pi.providers != null) {
                for (ProviderInfo cpi : pi.providers) {
                    subscriber.onNext(
                            new AppInfo(AppInfo.INFO_TYPE_PROVIDERS, simplifyName(cpi.name, pi.packageName)));
                }
            }
        }

        @MonitorMe
        private void receivers(@NonNull Subscriber<? super AppInfo> subscriber, @NonNull PackageInfo pi) {
            if (pi.receivers != null) {
                for (ActivityInfo ri : pi.receivers) {
                    subscriber.onNext(
                            new AppInfo(AppInfo.INFO_TYPE_PROVIDERS, simplifyName(ri.name, pi.packageName)));
                }
            }
        }

        @MonitorMe
        private String simplifyName(@NonNull String name, @NonNull String packageName) {
            if (name.startsWith(packageName)) {
                return name.substring(packageName.length());
            }
            return name;

        }
    }
}
