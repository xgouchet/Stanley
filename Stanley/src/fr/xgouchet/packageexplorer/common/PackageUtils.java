package fr.xgouchet.packageexplorer.common;

import java.io.File;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.view.View;
import android.view.View.OnClickListener;
import de.neofonie.mobile.app.android.widget.crouton.Crouton;
import de.neofonie.mobile.app.android.widget.crouton.Style;
import fr.xgouchet.packageexplorer.model.AsyncManifestExporter;
import fr.xgouchet.packageexplorer.model.AsyncManifestExporter.ManifestExporterListener;

public class PackageUtils {

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

	public static void exportManifest(final Activity activity,
			final PackageInfo pkg) {
		ManifestExporterListener listener;
		listener = new ManifestExporterListener() {

			@Override
			public void onExportError(final Exception exception) {
				Crouton.showText(activity,
						"An error occured while exporting the manifest",
						Style.ALERT);
			}

			@Override
			public void onManifestExported(final File file) {
				Crouton crouton = Crouton.makeText(activity,
						"The manifest was saved in your Download folder:\n "
								+ file.getName()
								+ "\nClick here to open it now", Style.CONFIRM);
				crouton.show();
				crouton.getView().setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(final View v) {
						Intent intent = new Intent(Intent.ACTION_VIEW);
						intent.setDataAndType(Uri.fromFile(file), "text/xml");
						activity.startActivity(Intent.createChooser(intent,
								null));
					}
				});
			}
		};
		AsyncManifestExporter exporter = new AsyncManifestExporter(activity,
				listener);
		exporter.execute(pkg);
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

	/**
	 * 
	 */
	public static List<ResolveInfo> getMainActivities(final Context context,
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
	 * 
	 */
	public static Intent getResolvedIntent(final ResolveInfo info) {
		Intent intent = new Intent(Intent.ACTION_MAIN);
		intent.addCategory(Intent.CATEGORY_LAUNCHER);
		intent.setClassName(info.activityInfo.packageName,
				info.activityInfo.name);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
				| Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
		return intent;
	}

	/**
	 * 
	 * @param context
	 * @param packageInfo
	 */
	public static boolean isPackageInstalled(final Context context,
			final PackageInfo packageInfo) {
		PackageManager pm = context.getPackageManager();
		try {
			pm.getPackageInfo(packageInfo.packageName,
					PackageManager.GET_META_DATA);
		} catch (NameNotFoundException e) {
			return false;
		}
		return true;
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

}
