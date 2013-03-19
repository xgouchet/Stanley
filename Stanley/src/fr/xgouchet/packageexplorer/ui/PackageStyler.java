package fr.xgouchet.packageexplorer.ui;

import fr.xgouchet.packageexplorer.model.SdkInfo;
import android.content.pm.FeatureInfo;
import android.content.pm.Signature;

public class PackageStyler {

	public static String getSdkInfo(int position, SdkInfo sdk) {
		String res;
		if (position == 0) {
			res = "Minimum : "
					+ ((sdk.minSdkVersion > 0) ? sdk.minSdkVersion : "?");
		} else if (position == 1) {
			res = "Target : "
					+ ((sdk.targetSdkVersion > 0) ? sdk.targetSdkVersion : "?");
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
			name = name.substring(0, 1).toUpperCase() + name.substring(1);

			builder.append(name);
		}

		return builder.toString();
	}

	public static String getSignature(Signature signature) {
		return signature.toCharsString();

	}
}
