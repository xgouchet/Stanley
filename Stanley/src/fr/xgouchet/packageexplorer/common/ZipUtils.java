package fr.xgouchet.packageexplorer.common;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.Deflater;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

public final class ZipUtils {

	public static final int MESSAGE_FILES_ARCHIVED = 42;
	public static final int MESSAGE_ERROR = 666;
	public static final int MESSAGE_CANCEL = 10;

	public final class ZipThread extends Thread {

		public ZipThread() {
			setName("Zipping thread");
			mCanceled = false;
		}

		public void setHandler(Handler handler) {
			mHandler = handler;
		}

		public void setFiles(List<File> files) {
			mFiles = files;
		}

		public void setOutputFile(File outputFile) {
			mOutputFile = outputFile;
		}

		@Override
		public void run() {
			super.run();

			if (archiveFiles(mOutputFile, Deflater.BEST_COMPRESSION, mFiles)) {
				sendMessage(MESSAGE_FILES_ARCHIVED);
			} else {
				if (mCanceled) {
					sendMessage(MESSAGE_CANCEL);
				} else {
					sendMessage(MESSAGE_ERROR);
				}
			}
		}

		/**
		 * @param what
		 *            User defined message code
		 */
		protected void sendMessage(int what) {
			Message msg = Message.obtain(mHandler);
			msg.what = what;
			msg.sendToTarget();
		}

		/**
		 * Archive files in a zip format
		 * 
		 * @param outputFile
		 *            the file to write to
		 * @param compression
		 *            the compression level (from -1 = default to 8 = max)
		 * @param entries
		 *            the files / folders to archive
		 * @return if the archive was made successfully
		 */
		protected boolean archiveFiles(File outputFile, int compression,
				List<File> entries) {

			OutputStream outputStream;
			ZipOutputStream zipOutputStream = null;

			boolean result;

			result = false;
			try {
				outputStream = new FileOutputStream(outputFile);
				zipOutputStream = new ZipOutputStream(new BufferedOutputStream(
						outputStream));
				zipOutputStream.setLevel(compression);

				for (File file : entries) {
					addFileToArchive(zipOutputStream, file);
					if (mCanceled) {
						break;
					}
				}

				zipOutputStream.close();

				result = !mCanceled;
			} catch (FileNotFoundException e) {
				Log.w("Zip",
						"Unable to write to output file "
								+ outputFile.getName());
			} catch (IOException e) {
				Log.w("Zip", "Unable to write output file");
			}

			if (mCanceled) {
				mOutputFile.delete();
			}

			return result;
		}

		/**
		 * Recursively adds a file/folder to a zip output stream
		 * 
		 * @param outputStream
		 *            the zip output stream
		 * @param file
		 *            the file to add
		 */
		protected void addFileToArchive(ZipOutputStream outputStream, File file) {

			if (!mCanceled) {
				if (file.isDirectory()) {
					addDirectoryToArchive(outputStream, file);
				} else {

					int length;
					byte[] buffer = new byte[1024];

					try {
						FileInputStream inputStream = new FileInputStream(file);
						ZipEntry zipEntry = new ZipEntry(file.getName());
						outputStream.putNextEntry(zipEntry);

						while ((length = inputStream.read(buffer)) > 0) {
							outputStream.write(buffer, 0, length);
							if (mCanceled) {
								break;
							}
						}

						outputStream.closeEntry();
						inputStream.close();
					} catch (FileNotFoundException e) {
						Log.w("Zip", "Unable to read file " + file.getName());
					} catch (IOException e) {
						Log.w("Zip", "Unable to write output file");
					}
				}
			}
		}

		/**
		 * Recursively adds a directory's children to a zip output stream
		 * 
		 * @param outputStream
		 *            the zip output stream
		 * @param folder
		 *            the folder to add
		 */
		protected void addDirectoryToArchive(ZipOutputStream outputStream,
				File folder) {

			File[] files = folder.listFiles();

			for (File file : files) {
				addFileToArchive(outputStream, file);
				if (mCanceled) {
					break;
				}
			}

		}

		public void cancel() {
			mCanceled = true;
		}

		protected File mOutputFile;
		protected Handler mHandler;
		protected List<File> mFiles;
		protected boolean mCanceled;

	}

	public static ZipThread createZipThread(File outputFile, Handler handler,
			List<File> files) {
		ZipThread thread = (new ZipUtils()).new ZipThread();

		thread.setOutputFile(outputFile);
		thread.setHandler(handler);
		thread.setFiles(files);

		return thread;
	}

	/**
	 * Extracts the source zip file into the given folder
	 * 
	 * @param srcFile
	 * @param destFolder
	 * @throws ZipException
	 *             if a Zip error occurs
	 * @throws IllegalStateException
	 * @throws IOException
	 */
	public static void extractZipFile(File srcFile, File destFolder)
			throws ZipException, IOException {
		ZipFile zipFile;
		ZipEntry entry;
		Enumeration<? extends ZipEntry> entries;

		zipFile = new ZipFile(srcFile);
		entries = zipFile.entries();

		while (entries.hasMoreElements()) {
			entry = entries.nextElement();
			extractZipEntry(entry, zipFile, destFolder);
		}

		zipFile.close();
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
	public static boolean extractZipEntry(ZipEntry entry, ZipFile file,
			File destFolder) throws FileNotFoundException, IOException,
			IllegalStateException {

		File outFile = new File(destFolder, entry.getName());
		FileOutputStream output;
		InputStream input;
		int n;
		byte[] buffer = new byte[1024];

		if (entry.isDirectory()) {
			return outFile.mkdirs();
		} else {
			outFile.getParentFile().mkdirs();

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

	private ZipUtils() {
	}
}
