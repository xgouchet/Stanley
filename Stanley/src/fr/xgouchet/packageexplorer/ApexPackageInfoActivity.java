package fr.xgouchet.packageexplorer;

import java.io.IOException;
import java.util.zip.ZipException;

import javax.xml.parsers.ParserConfigurationException;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import fr.xgouchet.packageexplorer.common.Constants;
import fr.xgouchet.packageexplorer.common.ManifestUtils;
import fr.xgouchet.packageexplorer.model.ManifestInfo;
import fr.xgouchet.packageexplorer.ui.adapter.PackageInfoAdapter;

public class ApexPackageInfoActivity extends Activity implements
		OnChildClickListener {

	/**
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	protected void onCreate(Bundle savedInstanceState) {
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
	}

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
		list.setOnChildClickListener(this);

	}

	public boolean onChildClick(ExpandableListView parent, View v,
			int groupPosition, int childPosition, long id) {
		// Log.i("Click", "Group : " + groupPosition + " / Child : "
		// + childPosition);

		if (groupPosition >= PackageInfoAdapter.INDEX_ACTIVITIES) {
			Intent appItem = new Intent(this, ApexAppItemInfoActivity.class);
			
			startActivity(appItem);
		}
		return true;
	}

	protected PackageManager mPackageManager;
	protected PackageInfo mPackageInfo;
	protected ApplicationInfo mAppInfo;

	protected ManifestInfo mManifest;

	protected PackageInfoAdapter mAdapter;

}
