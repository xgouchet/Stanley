package fr.xgouchet.packageexplorer.common;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import android.content.Context;
import android.content.pm.PackageInfo;

public class ResourcesUtils {

	public static File exportPackageResources(final Context context,
			final PackageInfo info) throws IOException {
		String srcPackage = info.applicationInfo.publicSourceDir;
		File apkFile = new File(srcPackage);
		File dstFolder = context.getDir(info.packageName, Context.MODE_PRIVATE);

		ZipFile zipFile;
		ZipEntry entry;
		Enumeration<? extends ZipEntry> entries;

		zipFile = new ZipFile(apkFile);
		entries = zipFile.entries();

		while (entries.hasMoreElements()) {
			entry = entries.nextElement();
			if (entry.isDirectory()) {
				continue;
			}

			if (entry.getName().endsWith(".png")) {
				extractZipEntry(entry, zipFile, dstFolder, false);
			}
		}

		zipFile.close();

		return dstFolder;

	}

	/**
	 * 
	 * @param entry
	 *            the zip entry to extract
	 * @param file
	 *            the zip file to extract from
	 * @param destFolder
	 *            the destination folder to extract to
	 * @return if the extraction was successful
	 * @throws FileNotFoundException
	 *             if the output file can't be found
	 * @throws IOException
	 *             if an IO error occured
	 * @throws IllegalStateException
	 *             if the zip has been closed before extracting the file
	 */
	protected static boolean extractZipEntry(final ZipEntry entry,
			final ZipFile file, final File destFolder,
			final boolean keepHierarchy) throws FileNotFoundException,
			IOException, IllegalStateException {

		String name = entry.getName();
		File outFile = new File(destFolder, name);
		FileOutputStream output;
		InputStream input;
		int n;
		byte[] buffer = new byte[1024];

		if (entry.isDirectory()) {
			return true;
		} else {
			File parent = outFile.getParentFile();
			parent.mkdirs();
			input = file.getInputStream(entry);
			output = new FileOutputStream(outFile);

			while ((n = input.read(buffer, 0, 1024)) >= 0) {
				output.write(buffer, 0, n);
			}

			input.close();
			output.close();
			return true;
		}

	}
}
