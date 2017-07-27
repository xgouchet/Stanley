package fr.xgouchet.packageexplorer.ui.activities;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import fr.xgouchet.packageexplorer.R;
import fr.xgouchet.packageexplorer.applist.AppViewModel;
import fr.xgouchet.packageexplorer.ui.fragments.AppDetailFragment;


public class OldAppDetailsActivity extends AppCompatActivity {

    public static final String EXTRA_PACKAGE_NAME = "package_name";
    private static final String TAG_DETAILS = "details";
    private AppViewModel app;

    public static void startWithApp(@NonNull Context context, @NonNull AppViewModel app) {
        Intent intent = buildIntent(context, app);
        context.startActivity(intent);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static void startWithAppAndTransition(@NonNull Activity activity, @NonNull AppViewModel app, @NonNull View icon) {
        Intent intent = buildIntent(activity, app);
        icon.setTransitionName("icon");
        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(activity, icon, "icon");
        activity.startActivity(intent, options.toBundle());
    }

    @NonNull
    private static Intent buildIntent(@NonNull Context context, @NonNull AppViewModel app) {
        Intent intent = new Intent(context, OldAppDetailsActivity.class);
        intent.putExtra("package_name", app.getPackageName());
        return intent;
    }

    Toolbar toolbar;

    ImageView icon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // handle intent
        handleIntent();
        if (app == null) {
            Toast.makeText(this, "Impossibru !", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // set title
        setContentView(R.layout.activity_app_details);

        // action bar
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setSubtitle(app.getPackageName());
        }
        icon.setImageDrawable(app.getIcon());
        setTitle(app.getTitle());


        // details fragment
        Fragment fragment;
        fragment = getSupportFragmentManager().findFragmentByTag(TAG_DETAILS);
        if (fragment == null) {
            fragment = AppDetailFragment.forApp(app.getPackageName());
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, fragment, TAG_DETAILS)
                    .commit();
        }
    }

    private void handleIntent() {
        Intent intent = getIntent();

        String packageName = intent.getStringExtra(EXTRA_PACKAGE_NAME);
        if (!TextUtils.isEmpty(packageName)) {
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                supportFinishAfterTransition();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
