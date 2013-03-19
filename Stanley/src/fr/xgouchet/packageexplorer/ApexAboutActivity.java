package fr.xgouchet.packageexplorer;

import android.os.Bundle;
import fr.xgouchet.androidlib.ui.activity.AboutActivity;

public class ApexAboutActivity extends AboutActivity {

	protected void onCreate(final Bundle savedState) {
		super.onCreate(savedState);
		setContentView(R.layout.layout_about);
	}
}
