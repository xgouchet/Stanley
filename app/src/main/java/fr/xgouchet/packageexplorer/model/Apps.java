package fr.xgouchet.packageexplorer.model;

import android.content.ComponentName;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.FeatureInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PermissionInfo;
import android.content.pm.ProviderInfo;
import android.content.pm.ServiceInfo;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.util.Log;

import org.reactivestreams.Subscriber;

import java.text.DateFormat;
import java.util.Locale;

import fr.xgouchet.packageexplorer.R;

/**
 * @author Xavier Gouchet
 */
public class Apps {
    private static final DateFormat DATE_FORMAT = DateFormat
            .getDateInstance(DateFormat.LONG);
//
//
//    public static class Infos implements Observable.OnSubscribe<AppInfo> {
//
//        public static final int FULL_INFO_FLAGS = PackageManager.GET_ACTIVITIES
//                | PackageManager.GET_GIDS
//                | PackageManager.GET_CONFIGURATIONS
//                | PackageManager.GET_INSTRUMENTATION
//                | PackageManager.GET_PERMISSIONS
//                | PackageManager.GET_PROVIDERS
//                | PackageManager.GET_RECEIVERS
//                | PackageManager.GET_SERVICES
//                | PackageManager.GET_SIGNATURES;
//
        private  Context context;
        private String packageName;
//
//        public Infos(@NonNull Context context, @NonNull String packageName) {
//            this.context = context;
//            this.packageName = packageName;
//        }
//
//        @Override
//        public void call(Subscriber<? super AppInfo> subscriber) {
//
//            PackageManager pm = context.getPackageManager();
//            PackageInfo pi;
//            ApplicationInfo ai;
//            try {
//                pi = pm.getPackageInfo(packageName, FULL_INFO_FLAGS);
//                ai = pm.getApplicationInfo(packageName, 0);
//            } catch (PackageManager.NameNotFoundException e) {
//                subscriber.onError(e);
//                return;
//            }
//
//            globalInfos(subscriber, pi, ai);
//            features(subscriber, pi);
//            customPermissions(subscriber, pi);
//            permissions(subscriber, pi);
//            activities(subscriber, pm, pi);
//            services(subscriber, pi);
//            providers(subscriber, pi);
//            receivers(subscriber, pi);
//
//            subscriber.onCompleted();
//        }

//        private void globalInfos(@NonNull Subscriber<? super AppInfo> subscriber, @NonNull PackageInfo pi, @NonNull ApplicationInfo ai) {
//
////            subscriber.onNext(new AppInfo(AppInfo.INFO_TYPE_GLOBAL, pi.packageName, false));
//            subscriber.onNext(new AppInfo(AppInfo.INFO_TYPE_GLOBAL, "Version Code : " + pi.versionCode, false));
//            subscriber.onNext(new AppInfo(AppInfo.INFO_TYPE_GLOBAL, "Version Name : \"" + pi.versionName + "\"", false));
//            subscriber.onNext(new AppInfo(AppInfo.INFO_TYPE_GLOBAL, "Installed  : " + DATE_FORMAT.format(new Date(pi.firstInstallTime)), false));
//            subscriber.onNext(new AppInfo(AppInfo.INFO_TYPE_GLOBAL, "Updated  : " + DATE_FORMAT.format(new Date(pi.lastUpdateTime)), false));
//            subscriber.onNext(new AppInfo(AppInfo.INFO_TYPE_GLOBAL, "Target SDK : " + ((ai.targetSdkVersion > 0) ? ai.targetSdkVersion : "?"), false));
//
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                String installLocation = null;
//                switch (pi.installLocation) {
//                    case PackageInfo.INSTALL_LOCATION_AUTO:
//                        installLocation = "Install Location : Auto";
//                        break;
//                    case PackageInfo.INSTALL_LOCATION_INTERNAL_ONLY:
//                        installLocation = "Install Location : Internal";
//                        break;
//                    case PackageInfo.INSTALL_LOCATION_PREFER_EXTERNAL:
//                        installLocation = "Install Location : External (if possible)";
//                        break;
//                }
//                if (installLocation != null) {
//                    subscriber.onNext(new AppInfo(AppInfo.INFO_TYPE_GLOBAL, installLocation, false));
//                }
//            }
//        }

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

        private String simplifyName(@NonNull String name, @NonNull String packageName) {
            if (name.startsWith(packageName)) {
                return name.substring(packageName.length());
            }
            return name;

        }
    }

