package fr.xgouchet.packageexplorer;

import java.io.IOException;
import java.util.zip.ZipException;

import javax.xml.parsers.ParserConfigurationException;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;
import fr.xgouchet.packageexplorer.common.Constants;
import fr.xgouchet.packageexplorer.common.ManifestUtils;
import fr.xgouchet.packageexplorer.common.PackageUtils;
import fr.xgouchet.packageexplorer.model.ManifestInfo;
import fr.xgouchet.packageexplorer.ui.adapter.PackageInfoAdapter;

public class StanleyPackageInfoActivity extends Activity {

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

		int flags = mAppInfo.flags;
		if ((flags & ApplicationInfo.FLAG_SYSTEM) == ApplicationInfo.FLAG_SYSTEM) {
			menu.setGroupVisible(R.id.group_installed_apps, false);
		}

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
		case R.id.action_uninstall:
			startActivity(PackageUtils.uninstallPackageIntent(mPackageInfo));
			break;
		case R.id.action_display_info:
			startActivity(PackageUtils.applicationInfoIntent(mPackageInfo));
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

		((TextView) findViewById(R.id.textAppName)).setText(mPackageManager
				.getApplicationLabel(mAppInfo));
		((ImageView) findViewById(R.id.imageAppIcon))
				.setImageDrawable(mPackageManager.getApplicationIcon(mAppInfo));

		mAdapter = new PackageInfoAdapter(getApplicationContext(),
				mPackageInfo, mManifest);
		ExpandableListView list;
		list = ((ExpandableListView) findViewById(R.id.listPkgInfo));
		list.setAdapter(mAdapter);
	}

	protected PackageManager mPackageManager;
	protected PackageInfo mPackageInfo;
	protected ApplicationInfo mAppInfo;

	protected ManifestInfo mManifest;

	protected PackageInfoAdapter mAdapter;

}
