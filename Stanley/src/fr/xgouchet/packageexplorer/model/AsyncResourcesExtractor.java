package fr.xgouchet.packageexplorer.model;

import java.io.File;

import android.app.Activity;
import android.content.pm.PackageInfo;
import android.os.AsyncTask;
import fr.xgouchet.packageexplorer.common.ResourcesUtils;

public class AsyncResourcesExtractor extends AsyncTask<PackageInfo, Void, File> {

	public static interface ResourcesExtractorListener {
		public void onResourcesExctracted(File file);

		public void onExtractionError(Exception exception);
	}

	public AsyncResourcesExtractor(final Activity activity,
			final ResourcesExtractorListener listener) {
		mActivity = activity;
		mListener = listener;
	}

	@Override
	protected File doInBackground(final PackageInfo... params) {
		File dest = null;

		try {
			dest = ResourcesUtils.exportPackageResources(mActivity, params[0]);
		} catch (Exception e) {
			mException = e;
		}

		return dest;
	}

	@Override
	protected void onPostExecute(final File result) {
		super.onPostExecute(result);

		if (mException == null) {
			mListener.onResourcesExctracted(result);
		} else {
			mListener.onExtractionError(mException);
		}

	}

	private Exception mException;
	private final Activity mActivity;
	private final ResourcesExtractorListener mListener;
}
