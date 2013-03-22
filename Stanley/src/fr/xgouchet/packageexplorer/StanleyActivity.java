package fr.xgouchet.packageexplorer;

import android.content.pm.PackageInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import fr.xgouchet.packageexplorer.common.Constants;
import fr.xgouchet.packageexplorer.common.PackageUtils;
import fr.xgouchet.packageexplorer.ui.fragments.PackageInfoFragment;
import fr.xgouchet.packageexplorer.ui.fragments.PackageListFragment;

public class StanleyActivity extends FragmentActivity {

	/**
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Setup UI
		setContentView(R.layout.layout_main);

		// Fragmented UI : phone vs tablets
		if (findViewById(R.id.tabletFragmentContainer) == null) {
			mIsTwoPaned = false;
			mListFragment = new PackageListFragment();
			final FragmentTransaction transaction = getSupportFragmentManager()
					.beginTransaction();
			transaction.add(R.id.phoneFragmentContainer, mListFragment);
			transaction.commit();
		} else {
			mIsTwoPaned = true;
			mListFragment = (PackageListFragment) getSupportFragmentManager()
					.findFragmentById(R.id.packageListFragment);
			mListFragment.setActivateOnItemClick(true);
		}

	}

	public void showPackageInfo(final PackageInfo info) {
		PackageInfo fullInfo = PackageUtils.getFullPackageInfo(this, info);

		Bundle args = new Bundle();
		args.putParcelable(Constants.EXTRA_PACKAGE_INFO, fullInfo);
		showPackageInfo(args);
	}

	/**
	 * 
	 * @param arguments
	 */
	public void showPackageInfo(final Bundle arguments) {
		int containerId;
		final Fragment details = new PackageInfoFragment();
		details.setArguments(arguments);

		final FragmentTransaction transaction = getSupportFragmentManager()
				.beginTransaction();

		if (mIsTwoPaned) {
			containerId = R.id.tabletFragmentContainer;
		} else {
			containerId = R.id.phoneFragmentContainer;
			transaction.addToBackStack("detail");
		}

		transaction.replace(containerId, details).commit();
	}

	// UI
	private PackageListFragment mListFragment;
	private boolean mIsTwoPaned;

}