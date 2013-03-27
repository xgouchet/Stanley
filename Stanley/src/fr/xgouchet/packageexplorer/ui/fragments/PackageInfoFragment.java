package fr.xgouchet.packageexplorer.ui.fragments;

import java.util.List;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;
import fr.xgouchet.packageexplorer.R;
import fr.xgouchet.packageexplorer.StanleyActivity;
import fr.xgouchet.packageexplorer.common.Constants;
import fr.xgouchet.packageexplorer.common.PackageUtils;
import fr.xgouchet.packageexplorer.ui.adapter.PackageInfoAdapter;

public class PackageInfoFragment extends ListFragment {

	/**
	 * @see android.support.v4.app.ListFragment#onCreateView(android.view.LayoutInflater,
	 *      android.view.ViewGroup, android.os.Bundle)
	 */
	@Override
	public View onCreateView(final LayoutInflater inflater,
			final ViewGroup container, final Bundle savedInstanceState) {

		View root = inflater
				.inflate(R.layout.layout_app_info, container, false);

		setHasOptionsMenu(true);

		return root;
	}

	/**
	 * @see android.support.v4.app.ListFragment#onViewCreated(android.view.View,
	 *      android.os.Bundle)
	 */
	@Override
	public void onViewCreated(final View view, final Bundle savedState) {
		super.onViewCreated(view, savedState);

		// Data
		mActivity = (StanleyActivity) getActivity();
		mPackageManager = mActivity.getPackageManager();
		mPackageInfo = getArguments().getParcelable(
				Constants.EXTRA_PACKAGE_INFO);
		mAppInfo = mPackageInfo.applicationInfo;

		mLauncherActivities = PackageUtils.getMainActivities(mActivity,
				mPackageInfo);

		setContentInfo();
	}

	/**
	 * @see android.support.v4.app.Fragment#onCreateOptionsMenu(android.view.Menu,
	 *      android.view.MenuInflater)
	 */
	@Override
	public void onCreateOptionsMenu(final Menu menu, final MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.package_context, menu);
		menu.findItem(R.id.action_uninstall).setVisible(false);
	}

	/**
	 * @see android.app.Activity#onOptionsItemSelected(android.view.MenuItem)
	 */
	@Override
	public boolean onOptionsItemSelected(final MenuItem item) {
		boolean res = true;

		switch (item.getItemId()) {
		case R.id.action_display_info:
			startActivity(PackageUtils.applicationInfoIntent(mPackageInfo));
			break;
		case R.id.action_display_store:
			startActivity(PackageUtils.applicationPlayStoreIntent(mPackageInfo));
			break;
		case R.id.action_export_manifest:
			PackageUtils.exportManifest(mActivity, mPackageInfo);
			break;
		case R.id.action_explore_resources:
			mActivity.browsePackageResources(mPackageInfo);
			break;
		default:
			res = super.onOptionsItemSelected(item);
			break;
		}

		return res;
	}

	/**
	 * 
	 */
	private void setContentInfo() {
		boolean hideOpen, hideUninstall;
		int flags = mAppInfo.flags;

		hideUninstall = ((flags & ApplicationInfo.FLAG_SYSTEM) == ApplicationInfo.FLAG_SYSTEM);
		hideOpen = (mLauncherActivities.size() == 0);
		if (hideOpen && hideUninstall) {
			getView().findViewById(R.id.layoutOpenUninstall).setVisibility(
					View.GONE);
		} else if (hideUninstall) {
			getView().findViewById(R.id.buttonUninstall).setVisibility(
					View.GONE);
		} else if (hideOpen) {
			getView().findViewById(R.id.buttonOpen).setVisibility(View.GONE);
		}

		getView().findViewById(R.id.buttonOpen).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(final View view) {
						onOpenPackage(view);
					}
				});

		getView().findViewById(R.id.buttonUninstall).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(final View view) {
						onUninstallPackage(view);
					}
				});

		String appLabel = mPackageManager.getApplicationLabel(mAppInfo)
				.toString();
		mActivity.setTitle(mActivity
				.getString(R.string.title_package, appLabel));

		((TextView) getView().findViewById(R.id.textAppName)).setText(appLabel);
		((TextView) getView().findViewById(R.id.textSubTitle))
				.setText(mPackageInfo.packageName);
		getView().findViewById(R.id.textSubTitle).setSelected(true);

		((ImageView) getView().findViewById(R.id.imageAppIcon))
				.setImageDrawable(mPackageManager.getApplicationIcon(mAppInfo));

		mAdapter = new PackageInfoAdapter(mActivity, mPackageInfo);
		ExpandableListView list;
		list = ((ExpandableListView) getView().findViewById(android.R.id.list));
		list.setAdapter(mAdapter);
	}

	/**
	 * @param view
	 */
	private void onOpenPackage(final View view) {
		int count = mLauncherActivities.size();

		if (count == 1) {
			// launch the first
			startActivity(PackageUtils.getResolvedIntent(mLauncherActivities
					.get(0)));
		} else {
			// Prompt for one activity
			FragmentManager fm = mActivity.getSupportFragmentManager();
			ResolveInfoFragment resolveDialog = new ResolveInfoFragment();
			Bundle args = new Bundle();
			args.putParcelableArray(Constants.EXTRA_RESOLVE_INFO,
					mLauncherActivities.toArray(new ResolveInfo[count]));
			resolveDialog.setArguments(args);
			resolveDialog.show(fm, "resolveDialog");
		}
	}

	/**
	 * 
	 * @param view
	 */
	private void onUninstallPackage(final View view) {
		startActivity(PackageUtils.uninstallPackageIntent(mPackageInfo));
	}

	/**
	 * @return
	 */
	public PackageInfo getPackageInfo() {
		return mPackageInfo;
	}

	protected StanleyActivity mActivity;
	protected PackageManager mPackageManager;

	protected PackageInfo mPackageInfo;
	protected ApplicationInfo mAppInfo;

	protected PackageInfoAdapter mAdapter;

	protected List<ResolveInfo> mLauncherActivities;
}
