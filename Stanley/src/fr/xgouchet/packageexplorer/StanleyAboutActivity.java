package fr.xgouchet.packageexplorer;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.view.MenuItem;
import fr.xgouchet.androidlib.ui.activity.AboutActivity;

public class StanleyAboutActivity extends AboutActivity {

	/**
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	@Override
	protected void onCreate(final Bundle savedState) {
		super.onCreate(savedState);
		setContentView(R.layout.layout_about);

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
}
