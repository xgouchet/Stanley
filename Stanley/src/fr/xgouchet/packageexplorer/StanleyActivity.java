package fr.xgouchet.packageexplorer;

import android.content.pm.PackageInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentManager.OnBackStackChangedListener;
import android.support.v4.app.FragmentTransaction;
import android.view.MenuItem;
import de.neofonie.mobile.app.android.widget.crouton.Crouton;
import fr.xgouchet.packageexplorer.common.Constants;
import fr.xgouchet.packageexplorer.common.PackageUtils;
import fr.xgouchet.packageexplorer.ui.fragments.PackageInfoFragment;
import fr.xgouchet.packageexplorer.ui.fragments.PackageListFragment;
import fr.xgouchet.packageexplorer.ui.fragments.ResourcesExplorerFragment;

public class StanleyActivity extends FragmentActivity implements
		OnBackStackChangedListener {

	// UI
	private PackageListFragment mListFragment;

	private boolean mIsTwoPaned;

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
			transaction.add(R.id.phoneFragmentContainer, mListFragment,
					Constants.TAG_FRAGMENT_LIST);
			transaction.commit();
		} else {
			mIsTwoPaned = true;
			mListFragment = (PackageListFragment) getSupportFragmentManager()
					.findFragmentById(R.id.packageListFragment);
		}

		getSupportFragmentManager().addOnBackStackChangedListener(this);
	}

	@Override
	protected void onResume() {
		super.onResume();

		FragmentManager fragMgr = getSupportFragmentManager();

		PackageInfoFragment infoFragment = (PackageInfoFragment) fragMgr
				.findFragmentByTag(Constants.TAG_FRAGMENT_DETAILS);

		if (infoFragment != null) {
			if (!PackageUtils.isPackageInstalled(this,
					infoFragment.getPackageInfo())) {
				final FragmentTransaction transaction = fragMgr
						.beginTransaction();
				transaction.remove(infoFragment);
				transaction.commit();
			}
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		Crouton.clearCroutonsForActivity(this);
	}

	@Override
	protected void onSaveInstanceState(final Bundle outState) {
	}

	@Override
	public void onBackStackChanged() {

		boolean hasBackStack = (getSupportFragmentManager()
				.getBackStackEntryCount() > 0);
		getActionBar().setDisplayHomeAsUpEnabled(hasBackStack && !mIsTwoPaned);

	}

	@Override
	public boolean onOptionsItemSelected(final MenuItem item) {
		boolean res = true;

		switch (item.getItemId()) {
		case android.R.id.home:
			getSupportFragmentManager().popBackStack();
			break;
		default:
			res = super.onOptionsItemSelected(item);
			break;
		}

		return res;
	}

	public void browsePackageResources(final PackageInfo info) {

		int containerId;
		Bundle args = new Bundle();
		args.putParcelable(Constants.EXTRA_PACKAGE_INFO, info);

		final Fragment resources = new ResourcesExplorerFragment();
		resources.setArguments(args);

		final FragmentTransaction transaction = getSupportFragmentManager()
				.beginTransaction();

		if (mIsTwoPaned) {
			containerId = R.id.tabletFragmentContainer;
		} else {
			containerId = R.id.phoneFragmentContainer;
			transaction.addToBackStack(Constants.TAG_FRAGMENT_RESOURCES);
		}

		transaction.replace(containerId, resources,
				Constants.TAG_FRAGMENT_RESOURCES);
		transaction.commit();

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
			transaction
					.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
			transaction.addToBackStack(Constants.TAG_FRAGMENT_DETAILS);
		}

		transaction.replace(containerId, details,
				Constants.TAG_FRAGMENT_DETAILS);
		transaction.commit();

	}

	public void showPackageInfo(final PackageInfo info) {
		PackageInfo fullInfo = PackageUtils.getFullPackageInfo(this, info);

		Bundle args = new Bundle();
		args.putParcelable(Constants.EXTRA_PACKAGE_INFO, fullInfo);
		showPackageInfo(args);
	}

}
