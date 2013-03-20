package fr.xgouchet.packageexplorer.ui;

import java.util.Locale;

import android.content.pm.FeatureInfo;
import android.content.pm.PackageInfo;
import android.content.pm.Signature;
import fr.xgouchet.packageexplorer.model.ManifestInfo;
import fr.xgouchet.packageexplorer.model.SdkInfo;

public class PackageStyler {

	public static final int APP_INFO_PACKAGE = 0;
	public static final int APP_INFO_VERSION_CODE = 1;
	public static final int APP_INFO_VERSION_NAME = 2;
	public static final int APP_INFO_SDK_MIN = 3;
	public static final int APP_INFO_SDK_MAX = 4;
	public static final int APP_INFO_SDK_TARGET = 5;

	public static final int APP_INFO_COUnT = 6;

	public static int getAppInfoCount() {
		return APP_INFO_COUnT;
	}

	public static String getAppInfo(int position, PackageInfo packageInfo,
			ManifestInfo manifestInfo) {

		SdkInfo sdk = manifestInfo.usesSdk;

		String res;
		if (position == APP_INFO_PACKAGE) {
			res = "Package Name : \"" + packageInfo.packageName + "\"";
		} else if (position == APP_INFO_VERSION_CODE) {
			res = "Version Code : " + packageInfo.versionCode + "";
		} else if (position == APP_INFO_VERSION_NAME) {
			res = "Version Name : \"" + packageInfo.versionName + "\"";
		} else if (position == APP_INFO_SDK_MIN) {
			res = "Minimum SDK : " + ((sdk.minSdk > 0) ? sdk.minSdk : "?");
		} else if (position == APP_INFO_SDK_MAX) {
			res = "Maximum SDK : " + ((sdk.maxSdk > 0) ? sdk.maxSdk : "?");
		} else if (position == APP_INFO_SDK_TARGET) {
			res = "Target SDK : " + ((sdk.targetSdk > 0) ? sdk.targetSdk : "?");
		} else {
			res = "";
		}
		return res;
	}

	public static String getLocalName(String activityName, String packageName) {
		String name = activityName;

		if (name.startsWith(packageName)) {
			name = name.substring(packageName.length());
		}

		return name;
	}

	public static String getFeature(FeatureInfo info) {
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

	public static String getSignature(Signature signature) {
		return signature.toCharsString();

	}
}
