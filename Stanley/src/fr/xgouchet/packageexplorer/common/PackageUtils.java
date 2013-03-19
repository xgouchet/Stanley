package fr.xgouchet.packageexplorer.common;

import java.io.File;
import java.io.IOException;
import java.util.zip.ZipException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.util.Log;

public class PackageUtils {

	/**
	 * @param pkg
	 *            the package info
	 * @return the intent to launch to uninstall the given package
	 */
	public static Intent uninstallPluginIntent(final PackageInfo pkg) {
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

	/**
	 * @param ctx
	 *            the current application context
	 * @param pkg
	 *            the package to export
	 * @return the exported file
	 */
	public static File exportManifest(final Context ctx, final PackageInfo pkg) {
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
