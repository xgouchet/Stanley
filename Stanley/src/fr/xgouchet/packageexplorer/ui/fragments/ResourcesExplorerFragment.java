package fr.xgouchet.packageexplorer.ui.fragments;

import java.io.File;

import android.content.pm.PackageInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import de.neofonie.mobile.app.android.widget.crouton.Crouton;
import de.neofonie.mobile.app.android.widget.crouton.Style;
import fr.xgouchet.androidlib.data.FileUtils;
import fr.xgouchet.packageexplorer.R;
import fr.xgouchet.packageexplorer.common.Constants;
import fr.xgouchet.packageexplorer.model.AsyncResourcesExtractor;
import fr.xgouchet.packageexplorer.model.AsyncResourcesExtractor.ResourcesExtractorListener;
import fr.xgouchet.packageexplorer.ui.adapter.ResourcesAdapter;

public class ResourcesExplorerFragment extends Fragment implements
		ResourcesExtractorListener {

	@Override
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(final LayoutInflater inflater,
			final ViewGroup container, final Bundle savedInstanceState) {
		// build UI
		View root = inflater.inflate(R.layout.layout_resources, container,
				false);
		mResourcesListView = (ExpandableListView) root
				.findViewById(android.R.id.list);
		mResourcesListView.setEmptyView(root.findViewById(android.R.id.empty));

		setHasOptionsMenu(true);

		// Start loading resources
		PackageInfo info = getArguments().getParcelable(
				Constants.EXTRA_PACKAGE_INFO);
		AsyncResourcesExtractor extractor;
		extractor = new AsyncResourcesExtractor(getActivity(), this);
		extractor.execute(info);

		String appLabel = getActivity().getPackageManager()
				.getApplicationLabel(info.applicationInfo).toString();
		getActivity().setTitle(
				getActivity().getString(R.string.title_package, appLabel));
		return root;
	}

	/**
	 * @see android.support.v4.app.Fragment#onCreateOptionsMenu(android.view.Menu,
	 *      android.view.MenuInflater)
	 */
	@Override
	public void onCreateOptionsMenu(final Menu menu, final MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);

		inflater.inflate(R.menu.resources, menu);
	}

	@Override
	public boolean onOptionsItemSelected(final MenuItem item) {
		boolean res = true;
		switch (item.getItemId()) {
		case R.id.action_switch_background:
			mAdapter.switchBackground();
			break;
		case R.id.action_zoom_in:
			mAdapter.zoomIn();
			break;
		case R.id.action_zoom_out:
			mAdapter.zoomOut();
			break;
		case R.id.action_zoom_reset:
			mAdapter.zoomReset();
			break;
		default:
			res = super.onOptionsItemSelected(item);
			break;
		}

		return res;
	}

	/**
	 * @see fr.xgouchet.packageexplorer.model.AsyncResourcesExtractor.ResourcesExtractorListener#onExtractionError(java.lang.Exception)
	 */
	@Override
	public void onExtractionError(final Exception exception) {
		Crouton.showText(getActivity(), "Unable to retrieve package resources",
				Style.ALERT);
		getActivity().getSupportFragmentManager().popBackStack();
	}

	/**
	 * @see fr.xgouchet.packageexplorer.model.AsyncResourcesExtractor.ResourcesExtractorListener#onResourcesExctracted(java.io.File)
	 */
	@Override
	public void onResourcesExctracted(final File file) {
		mFolder = new File(file, "res");

		mAdapter = new ResourcesAdapter(getActivity(), mFolder);
		mResourcesListView.setAdapter(mAdapter);
	}

	/**
	 * @see android.support.v4.app.Fragment#onDestroy()
	 */
	@Override
	public void onDestroy() {
		super.onDestroy();
		FileUtils.deleteRecursiveFolder(mFolder);
	}

	private ExpandableListView mResourcesListView;
	private ResourcesAdapter mAdapter;
	private File mFolder;
}
