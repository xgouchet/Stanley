package fr.xgouchet.packageexplorer.ui;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

import android.content.pm.FeatureInfo;
import android.content.pm.PackageInfo;
import android.content.pm.Signature;

public class PackageStyler {

	public static final int APP_INFO_VERSION_CODE = 0;
	public static final int APP_INFO_VERSION_NAME = APP_INFO_VERSION_CODE + 1;

	public static final int APP_INFO_INSTALL_DATE = APP_INFO_VERSION_NAME + 1;
	public static final int APP_INFO_UPDATE_DATE = APP_INFO_INSTALL_DATE + 1;

	public static final int APP_INFO_SDK_MIN = APP_INFO_UPDATE_DATE + 1;
	public static final int APP_INFO_SDK_MAX = APP_INFO_SDK_MIN + 1;
	public static final int APP_INFO_SDK_TARGET = APP_INFO_SDK_MAX + 1;

	public static final int APP_INFO_COUNT = APP_INFO_SDK_TARGET + 1;

	private static final DateFormat DATE_FORMAT = DateFormat
			.getDateInstance(DateFormat.LONG);

	public static int getAppInfoCount() {
		return APP_INFO_COUNT;
	}

	public static String simplifyName(final String name,
			final String packageName) {
		String simpleName;

		if (name.startsWith(packageName)) {
			simpleName = name.substring(packageName.length());
		} else {
			simpleName = name;
		}

		return simpleName;
	}

	public static String getAppInfo(final int position,
			final PackageInfo packageInfo) {

		String res;
		if (position == APP_INFO_VERSION_CODE) {
			res = "Version Code : " + packageInfo.versionCode + "";
		} else if (position == APP_INFO_VERSION_NAME) {
			res = "Version Name : \"" + packageInfo.versionName + "\"";
		} else if (position == APP_INFO_INSTALL_DATE) {
			Date date = new Date(packageInfo.firstInstallTime);
			res = "Installed  : " + DATE_FORMAT.format(date) + "";
		} else if (position == APP_INFO_UPDATE_DATE) {
			Date date = new Date(packageInfo.lastUpdateTime);
			res = "Updated  : " + DATE_FORMAT.format(date) + "";
		} else if (position == APP_INFO_SDK_TARGET) {
			res = "Target SDK : "
					+ ((packageInfo.applicationInfo.targetSdkVersion > 0) ? packageInfo.applicationInfo.targetSdkVersion
							: "?");
		} else {
			res = "";
		}
		return res;
	}

	public static String getLocalName(final String activityName,
			final String packageName) {
		String name = activityName;

		if (name.startsWith(packageName)) {
			name = name.substring(packageName.length());
		}

		return name;
	}

	public static String getFeature(final FeatureInfo info) {
		StringBuilder builder = new StringBuilder();

		if (info.reqGlEsVersion != 0) {
			builder.append("OpenGL ES ");
			int maj, min;
			maj = info.reqGlEsVersion >> 16;
			min = info.reqGlEsVersion & 0xFFFF;

			builder.append(maj);
			builder.append(".");
			builder.append(min);
		} else {
			String name = info.name;
			if (name.startsWith("android.hardware.")) {
				name = name.substring(17);
			} else if (name.startsWith("android.software.")) {
				name = name.substring(17);
			}

			name = name.replaceAll("\\.", " ").trim();
			name = name.replaceAll("_", " ").trim();
			name = name.substring(0, 1).toUpperCase(Locale.getDefault())
					+ name.substring(1);

			builder.append(name);
		}

		return builder.toString();
	}

	public static String getSignature(final Signature signature) {
		return signature.toCharsString();

	}
}
