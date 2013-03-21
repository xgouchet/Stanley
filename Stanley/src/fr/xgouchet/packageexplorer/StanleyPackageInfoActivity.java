package fr.xgouchet.packageexplorer;

import java.io.IOException;
import java.util.List;
import java.util.zip.ZipException;

import javax.xml.parsers.ParserConfigurationException;

import android.annotation.TargetApi;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Build;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;
import fr.xgouchet.packageexplorer.common.Constants;
import fr.xgouchet.packageexplorer.common.ManifestUtils;
import fr.xgouchet.packageexplorer.common.PackageUtils;
import fr.xgouchet.packageexplorer.model.ManifestInfo;
import fr.xgouchet.packageexplorer.ui.ResolveInfoDialog;
import fr.xgouchet.packageexplorer.ui.adapter.PackageInfoAdapter;

public class StanleyPackageInfoActivity extends FragmentActivity {

	/**
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_app_info);

		mPackageManager = getPackageManager();
		mPackageInfo = getIntent().getParcelableExtra(
				Constants.EXTRA_PACKAGE_INFO);
		mAppInfo = mPackageInfo.applicationInfo;

		mActivities = PackageUtils.getMainActivities(this, mPackageInfo);

		try {
			mManifest = ManifestUtils.getManifestInfo(mPackageInfo, this);
		} catch (ZipException e) {
			mManifest = new ManifestInfo();
			e.printStackTrace();
		} catch (IOException e) {
			mManifest = new ManifestInfo();
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			mManifest = new ManifestInfo();
			e.printStackTrace();
		} catch (Exception e) {
			mManifest = new ManifestInfo();
			e.printStackTrace();
		}

		setContentInfo();

		if (VERSION.SDK_INT >= VERSION_CODES.HONEYCOMB) {
			getActionBar().setDisplayHomeAsUpEnabled(true);
			getActionBar().setDisplayShowHomeEnabled(true);
		}
	}

	/**
	 * @see android.app.Activity#onCreateOptionsMenu(android.view.Menu)
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.package_context, menu);

		menu.findItem(R.id.action_uninstall).setVisible(false);

		return true;
	}

	/**
	 * @see android.app.Activity#onOptionsItemSelected(android.view.MenuItem)
	 */
	@Override
	public boolean onOptionsItemSelected(final MenuItem item) {
		boolean res = true;

		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
			break;
		case R.id.action_display_info:
			startActivity(PackageUtils.applicationInfoIntent(mPackageInfo));
			break;
		case R.id.action_display_store:
			startActivity(PackageUtils.applicationPlayStoreIntent(mPackageInfo));
			break;
		case R.id.action_export_manifest:
			PackageUtils.exportManifest(this, mPackageInfo);
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
	protected void setContentInfo() {
		boolean hideOpen, hideUninstall;
		int flags = mAppInfo.flags;

		hideUninstall = ((flags & ApplicationInfo.FLAG_SYSTEM) == ApplicationInfo.FLAG_SYSTEM);
		hideOpen = (mActivities.size() == 0);
		if (hideOpen && hideUninstall) {
			findViewById(R.id.layoutOpenUninstall).setVisibility(View.GONE);
		} else if (hideUninstall) {
			findViewById(R.id.buttonUninstall).setVisibility(View.GONE);
		} else if (hideOpen) {
			findViewById(R.id.buttonOpen).setVisibility(View.GONE);
		}

		((TextView) findViewById(R.id.textAppName)).setText(mPackageManager
				.getApplicationLabel(mAppInfo));
		((ImageView) findViewById(R.id.imageAppIcon))
				.setImageDrawable(mPackageManager.getApplicationIcon(mAppInfo));

		mAdapter = new PackageInfoAdapter(getApplicationContext(),
				mPackageInfo, mManifest);
		ExpandableListView list;
		list = ((ExpandableListView) findViewById(android.R.id.list));
		list.setAdapter(mAdapter);
	}

	public void onOpenPackage(View view) {
		int count = mActivities.size();

		if (count == 1) {
			// launch the first
			startActivity(PackageUtils.getResolvedIntent(mActivities.get(0)));
		} else {
			// Prompt for one activity
			FragmentManager fm = getSupportFragmentManager();
			ResolveInfoDialog resolveDialog = new ResolveInfoDialog();
			Bundle args = new Bundle();
			args.putParcelableArray(Constants.EXTRA_RESOLVE_INFO,
					mActivities.toArray(new ResolveInfo[count]));
			resolveDialog.setArguments(args);
			resolveDialog.show(fm, "resolveDialog");
		}
	}

	public void onUninstallPackage(View view) {
		startActivity(PackageUtils.uninstallPackageIntent(mPackageInfo));
	}

	protected PackageManager mPackageManager;
	protected PackageInfo mPackageInfo;
	protected ApplicationInfo mAppInfo;

	protected ManifestInfo mManifest;

	protected PackageInfoAdapter mAdapter;

	protected List<ResolveInfo> mActivities;

}
