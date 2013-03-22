package fr.xgouchet.packageexplorer.model;

import java.io.File;
import java.io.IOException;
import java.util.zip.ZipException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import android.app.Activity;
import android.content.pm.PackageInfo;
import android.os.AsyncTask;
import fr.xgouchet.packageexplorer.common.ManifestUtils;

public class AsyncManifestExporter extends AsyncTask<PackageInfo, Void, File> {

	public static interface ManifestExporterListener {
		public void onManifestExported(File file);

		public void onManifestError(Exception exception);
	}

	public AsyncManifestExporter(final Activity activity,
			final ManifestExporterListener listener) {
		mActivity = activity;
		mListener = listener;
	}

	@Override
	protected File doInBackground(final PackageInfo... params) {
		File dest = null;

		try {
			dest = ManifestUtils.exportManifest(params[0], mActivity);
		} catch (ZipException e) {
			mException = e;
		} catch (IOException e) {
			mException = e;
		} catch (TransformerException e) {
			mException = e;
		} catch (ParserConfigurationException e) {
			mException = e;
		} catch (Exception e) {
			mException = e;
		}

		return dest;
	}

	@Override
	protected void onPostExecute(final File result) {
		super.onPostExecute(result);

		if (mException == null) {
			mListener.onManifestExported(result);
		} else {
			mListener.onManifestError(mException);
		}

	}

	private Exception mException;
	private final Activity mActivity;
	private final ManifestExporterListener mListener;
}
