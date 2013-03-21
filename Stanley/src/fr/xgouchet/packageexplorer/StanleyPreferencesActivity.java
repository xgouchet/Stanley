package fr.xgouchet.packageexplorer;

import android.annotation.TargetApi;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Build;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.view.MenuItem;
import fr.xgouchet.packageexplorer.common.Constants;
import fr.xgouchet.packageexplorer.common.Settings;

@SuppressWarnings("deprecation")
public class StanleyPreferencesActivity extends PreferenceActivity implements
		OnSharedPreferenceChangeListener {
	/**
	 * @see android.preference.PreferenceActivity#onCreate(android.os.Bundle)
	 */

	@Override
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	protected void onCreate(final Bundle icicle) {
		super.onCreate(icicle);

		getPreferenceManager().setSharedPreferencesName(Constants.PREFERENCES);

		addPreferencesFromResource(R.xml.prefs);

		getPreferenceManager().getSharedPreferences()
				.registerOnSharedPreferenceChangeListener(this);

		updateSummaries();

		if (VERSION.SDK_INT >= VERSION_CODES.HONEYCOMB) {
			getActionBar().setDisplayHomeAsUpEnabled(true);
			getActionBar().setDisplayShowHomeEnabled(true);
		}
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
		default:
			res = super.onOptionsItemSelected(item);
			break;
		}

		return res;
	}

	/**
	 * @see android.content.SharedPreferences.OnSharedPreferenceChangeListener#onSharedPreferenceChanged(android.content.SharedPreferences,
	 *      java.lang.String)
	 */
	@Override
	public void onSharedPreferenceChanged(
			final SharedPreferences sharedPreferences, final String key) {
		Settings.updateFromPreferences(sharedPreferences);
		updateSummaries();
	}

	/**
	 * Updates the summaries for every list
	 */
	protected void updateSummaries() {

	}
}
