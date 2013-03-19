package fr.xgouchet.packageexplorer;

import java.io.File;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import de.neofonie.mobile.app.android.widget.crouton.Crouton;
import de.neofonie.mobile.app.android.widget.crouton.Style;
import fr.xgouchet.packageexplorer.common.Constants;
import fr.xgouchet.packageexplorer.common.PackageUtils;
import fr.xgouchet.packageexplorer.common.Settings;
import fr.xgouchet.packageexplorer.ui.adapter.PackageListAdapter;

public class StanleyActivity extends Activity implements OnItemClickListener {

	/**
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.layout_main);

		mListView = (ListView) findViewById(R.id.listApps);
		mListView.setEmptyView(findViewById(R.id.emptyView));
		mListView.setFastScrollEnabled(true);

		mPackages = new LinkedList<PackageInfo>();
		mAdapter = new PackageListAdapter(this, mPackages);
		mListView.setAdapter(mAdapter);

		mListView.setOnCreateContextMenuListener(this);
		mListView.setOnItemClickListener(this);
	}

	/**
	 * @see android.app.Activity#onResume()
	 */
	@Override
	protected void onResume() {
		super.onResume();

		SharedPreferences prefs = getSharedPreferences(Constants.PREFERENCES,
				MODE_PRIVATE);
		// TODO refresh settings from prefs

		new Thread(mRefreshRunnable).start();
	}

	/**
	 * @see android.app.Activity#onCreateOptionsMenu(android.view.Menu)
	 */
	@Override
	public boolean onCreateOptionsMenu(final Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
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
			startActivity(new Intent(getBaseContext(),
					StanleyAboutActivity.class));
			break;
		case R.id.action_sort_by_name:
			Settings.sSortMethod = Constants.SORT_BY_NAME;
			refresh = true;
			break;
		case R.id.action_sort_by_package:
			Settings.sSortMethod = Constants.SORT_BY_PACKAGE;
			refresh = true;
			break;
		case R.id.action_sort_by_install:
			Settings.sSortMethod = Constants.SORT_BY_INSTALL;
			refresh = true;
			break;
		case R.id.action_sort_by_update:
			Settings.sSortMethod = Constants.SORT_BY_UPDATE;
			refresh = true;
			break;
		case R.id.action_settings:
			break;
		default:
			result = super.onOptionsItemSelected(item);
			break;
		}

		if (refresh) {
			new Thread(mRefreshRunnable).start();
			SharedPreferences prefs = getSharedPreferences(
					Constants.PREFERENCES, MODE_PRIVATE);
			// TODO save settings
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

		getMenuInflater().inflate(R.menu.package_context, menu);

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
			startActivity(PackageUtils.uninstallPluginIntent(mSelectedPackage));
			break;
		case R.id.action_explore_resources:
			break;
		case R.id.action_export_manifest:
			exportPackageManifest();
			break;
		case R.id.action_display_info:

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
		final Intent appInfo = new Intent(getApplicationContext(),
				StanleyPackageInfoActivity.class);

		appInfo.putExtra(Constants.EXTRA_PACKAGE_INFO,
				PackageUtils.getFullPackageInfo(this, info));
		startActivity(appInfo);
	}

	/**
	 * Refresh the list of packages, filter, sort and display it
	 */
	private void refreshData() {
		final PackageManager pm = getPackageManager();
		final List<PackageInfo> packages = pm.getInstalledPackages(0);

		filterPackages(packages);

		Collections.sort(packages, Constants.getComparator(pm));

		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				mPackages.clear();
				mPackages.addAll(packages);
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

	private void exportPackageManifest() {
		File res = PackageUtils.exportManifest(this, mSelectedPackage);
		if (res != null) {
			Crouton.showText(this,
					"The manifest was exported in your Download folder in the file "
							+ res.getName(), Style.INFO);
		} else {
			Crouton.showText(this,
					"An error occured while exporting the manifest",
					Style.ALERT);
		}
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

	protected PackageInfo mSelectedPackage;
	protected List<PackageInfo> mPackages;
	protected ListView mListView;
	protected PackageListAdapter mAdapter;
}
