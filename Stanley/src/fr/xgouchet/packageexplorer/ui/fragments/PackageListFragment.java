package fr.xgouchet.packageexplorer.ui.fragments;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import fr.xgouchet.packageexplorer.R;
import fr.xgouchet.packageexplorer.StanleyAboutActivity;
import fr.xgouchet.packageexplorer.StanleyActivity;
import fr.xgouchet.packageexplorer.StanleyPackageInfoActivity;
import fr.xgouchet.packageexplorer.StanleyPreferencesActivity;
import fr.xgouchet.packageexplorer.common.Constants;
import fr.xgouchet.packageexplorer.common.PackageUtils;
import fr.xgouchet.packageexplorer.common.Settings;
import fr.xgouchet.packageexplorer.ui.adapter.PackageListAdapter;

public class PackageListFragment extends Fragment implements
		OnItemClickListener {

	@Override
	public void onAttach(final Activity activity) {
		super.onAttach(activity);
		mActivity = (StanleyActivity) activity;
	}

	@Override
	public View onCreateView(final LayoutInflater inflater,
			final ViewGroup container, final Bundle savedInstanceState) {

		View root = inflater
				.inflate(R.layout.layout_app_list, container, false);

		mListView = (ListView) root.findViewById(android.R.id.list);
		mListView.setEmptyView(root.findViewById(android.R.id.empty));
		mListView.setFastScrollEnabled(true);

		mPackages = new LinkedList<PackageInfo>();
		mAdapter = new PackageListAdapter(mActivity, mPackages);
		mListView.setAdapter(mAdapter);

		mListView.setOnCreateContextMenuListener(this);
		mListView.setOnItemClickListener(this);

		mSortMethod = Settings.sDefaultSortMethod;

		setHasOptionsMenu(true);

		return root;
	}

	@Override
	public void onResume() {
		super.onResume();
		SharedPreferences prefs = mActivity.getSharedPreferences(
				Constants.PREFERENCES, Context.MODE_PRIVATE);
		Settings.updateFromPreferences(prefs);

		new Thread(mRefreshRunnable).start();
	}

	@Override
	public void onCreateOptionsMenu(final Menu menu, final MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.main, menu);
	}

	/**
	 * @see android.app.Activity#onOptionsItemSelected(android.view.MenuItem)
	 */
	@Override
	public boolean onOptionsItemSelected(final MenuItem item) {
		boolean result = true;
		boolean refresh = false;

		switch (item.getItemId()) {
		case R.id.action_about:
			startActivity(new Intent(mActivity, StanleyAboutActivity.class));
			break;
		case R.id.action_sort_by_name:
			mSortMethod = Constants.SORT_BY_NAME;
			refresh = true;
			break;
		case R.id.action_sort_by_package:
			mSortMethod = Constants.SORT_BY_PACKAGE;
			refresh = true;
			break;
		case R.id.action_sort_by_install:
			mSortMethod = Constants.SORT_BY_INSTALL;
			refresh = true;
			break;
		case R.id.action_sort_by_update:
			mSortMethod = Constants.SORT_BY_UPDATE;
			refresh = true;
			break;
		case R.id.action_settings:
			startActivity(new Intent(mActivity,
					StanleyPreferencesActivity.class));
			break;
		default:
			result = super.onOptionsItemSelected(item);
			break;
		}

		if (refresh) {
			mAdapter.clear();
			new Thread(mRefreshRunnable).start();
		}
		return result;
	}

	/**
	 * @see android.app.Activity#onCreateContextMenu(android.view.ContextMenu,
	 *      android.view.View, android.view.ContextMenu.ContextMenuInfo)
	 */
	@Override
	public void onCreateContextMenu(final ContextMenu menu, final View view,
			final ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, view, menuInfo);

		final AdapterContextMenuInfo info = (AdapterContextMenuInfo) menuInfo;

		mSelectedPackage = mPackages.get(info.position);

		mActivity.getMenuInflater().inflate(R.menu.package_context, menu);

		ApplicationInfo appInfo = mSelectedPackage.applicationInfo;

		if (appInfo != null) {
			int flags = appInfo.flags;
			if ((flags & ApplicationInfo.FLAG_SYSTEM) == ApplicationInfo.FLAG_SYSTEM) {
				menu.setGroupEnabled(R.id.group_installed_apps, false);
			}
		}
	}

	/**
	 * @see android.app.Activity#onContextItemSelected(android.view.MenuItem)
	 */
	@Override
	public boolean onContextItemSelected(final MenuItem item) {
		boolean res = true;

		switch (item.getItemId()) {
		case R.id.action_uninstall:
			startActivity(PackageUtils.uninstallPackageIntent(mSelectedPackage));
			break;
		case R.id.action_explore_resources:
			break;
		case R.id.action_export_manifest:
			PackageUtils.exportManifest(mActivity, mSelectedPackage);
			break;
		case R.id.action_display_info:
			startActivity(PackageUtils.applicationInfoIntent(mSelectedPackage));
			break;
		case R.id.action_display_store:
			startActivity(PackageUtils
					.applicationPlayStoreIntent(mSelectedPackage));
			break;
		default:
			res = super.onContextItemSelected(item);
			break;
		}
		return res;
	}

	/**
	 * @see android.widget.AdapterView.OnItemClickListener#onItemClick(android.widget.AdapterView,
	 *      android.view.View, int, long)
	 */
	@Override
	public void onItemClick(final AdapterView<?> parent, final View view,
			final int position, final long id) {
		final PackageInfo info = mAdapter.getItem(position);
		mActivity.showPackageInfo(info);


	}

	public void setActivateOnItemClick(final boolean activateOnClick) {
		// TODO Auto-generated method stub

	}

	/**
	 * Refresh the list of packages, filter, sort and display it
	 */
	private void refreshData() {
		final PackageManager pm = mActivity.getPackageManager();
		final List<PackageInfo> packages = pm.getInstalledPackages(0);

		filterPackages(packages);

		Collections.sort(packages, Constants.getComparator(pm, mSortMethod));

		mActivity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				mPackages.clear();
				mPackages.addAll(packages);
				mAdapter.setSortMethod(mSortMethod);
				mAdapter.notifyDataSetChanged();
			}
		});

	}

	/**
	 * Filter the pacakges based on the settings
	 */
	private void filterPackages(final List<PackageInfo> packages) {
		int flags;
		ApplicationInfo appInfo;
		List<PackageInfo> remove = new LinkedList<PackageInfo>();

		// filter
		for (PackageInfo pkg : mPackages) {
			appInfo = pkg.applicationInfo;

			if (appInfo != null) {
				flags = appInfo.flags;
				if ((flags & ApplicationInfo.FLAG_SYSTEM) == ApplicationInfo.FLAG_SYSTEM) {
					remove.add(pkg);
				}
			}
		}

		packages.removeAll(remove);
	}

	/**
	 * Runnable used to list all packages in background
	 */
	private Runnable mRefreshRunnable = new Runnable() {
		@Override
		public void run() {
			refreshData();
		}
	};

	protected int mSortMethod;
	protected StanleyActivity mActivity;
	protected PackageInfo mSelectedPackage;
	protected List<PackageInfo> mPackages;
	protected ListView mListView;
	protected PackageListAdapter mAdapter;

}
