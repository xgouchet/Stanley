package fr.xgouchet.packageexplorer.common;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.zip.ZipException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import de.neofonie.mobile.app.android.widget.crouton.Crouton;
import de.neofonie.mobile.app.android.widget.crouton.Style;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.util.Log;

public class PackageUtils {

	public static Intent getResolvedIntent(ResolveInfo info) {
		Intent intent = new Intent(Intent.ACTION_MAIN);
		intent.addCategory(Intent.CATEGORY_LAUNCHER);
		intent.setClassName(info.activityInfo.packageName,
				info.activityInfo.name);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
				| Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
		return intent;
	}

	public static List<ResolveInfo> getMainActivities(Context context,
			final PackageInfo pkg) {
		Intent intent = new Intent(Intent.ACTION_MAIN);
		intent.addCategory(Intent.CATEGORY_LAUNCHER);
		intent.setPackage(pkg.packageName);

		List<ResolveInfo> resolvedList;

		resolvedList = context.getPackageManager().queryIntentActivities(
				intent, 0);

		return resolvedList;
	}

	/**
	 * @param pkg
	 *            the package info
	 * @return the intent to show the given package in the Google Play Store
	 */
	public static Intent applicationOpenIntent(final PackageInfo pkg) {
		Intent intent = new Intent(Intent.ACTION_MAIN);
		intent.addCategory(Intent.CATEGORY_LAUNCHER);
		intent.setPackage(pkg.packageName);
		return intent;
	}

	/**
	 * @param pkg
	 *            the package info
	 * @return the intent to show the given package in the Google Play Store
	 */
	public static Intent applicationPlayStoreIntent(final PackageInfo pkg) {
		final Uri packageUri = Uri
				.parse("https://play.google.com/store/apps/details?id="
						+ pkg.packageName);
		return new Intent(Intent.ACTION_VIEW, packageUri);
	}

	/**
	 * @param pkg
	 *            the package info
	 * @return the intent to show the system app info for the given package
	 */
	public static Intent applicationInfoIntent(final PackageInfo pkg) {
		final Uri packageUri = Uri.parse("package:" + pkg.packageName);
		return new Intent(
				android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
				packageUri);
	}

	/**
	 * @param pkg
	 *            the package info
	 * @return the intent to uninstall the given package
	 */
	public static Intent uninstallPackageIntent(final PackageInfo pkg) {
		final Uri packageUri = Uri.parse("package:" + pkg.packageName);
		return new Intent(Intent.ACTION_DELETE, packageUri);
	}

	/**
	 * @param ctx
	 *            the current application context
	 * @param pkg
	 *            the package info
	 * @return the full package information
	 */
	public static PackageInfo getFullPackageInfo(final Context ctx,
			final PackageInfo pkg) {
		int flags;
		flags = 0;
		flags |= PackageManager.GET_ACTIVITIES;
		flags |= PackageManager.GET_GIDS;
		flags |= PackageManager.GET_CONFIGURATIONS;
		flags |= PackageManager.GET_INSTRUMENTATION;
		flags |= PackageManager.GET_PERMISSIONS;
		flags |= PackageManager.GET_PROVIDERS;
		flags |= PackageManager.GET_RECEIVERS;
		flags |= PackageManager.GET_SERVICES;
		flags |= PackageManager.GET_SIGNATURES;

		PackageInfo fullInfo;

		try {
			fullInfo = ctx.getPackageManager().getPackageInfo(pkg.packageName,
					flags);
		} catch (NameNotFoundException e) {
			fullInfo = pkg;
		}

		return fullInfo;
	}

	public static void exportManifest(final Activity activity,
			final PackageInfo pkg) {
		File res = exportManifestToFile(activity, pkg);
		if (res != null) {
			Crouton.showText(activity,
					"The manifest was exported in your Download folder in the file "
							+ res.getName(), Style.INFO);
		} else {
			Crouton.showText(activity,
					"An error occured while exporting the manifest",
					Style.ALERT);
		}
	}

	/**
	 * @param ctx
	 *            the current application context
	 * @param pkg
	 *            the package to export
	 * @return the exported file
	 */
	private static File exportManifestToFile(final Context ctx,
			final PackageInfo pkg) {
		File dest;
		try {
			dest = ManifestUtils.exportManifest(pkg, ctx);
		} catch (ZipException e) {
			dest = null;
			Log.e("Apex", e.getMessage());
		} catch (IOException e) {
			dest = null;
			Log.e("Apex", e.getMessage());
		} catch (TransformerException e) {
			dest = null;
			Log.e("Apex", e.getMessage());
		} catch (ParserConfigurationException e) {
			dest = null;
			Log.e("Apex", e.getMessage());
		}

		return dest;
	}

}
