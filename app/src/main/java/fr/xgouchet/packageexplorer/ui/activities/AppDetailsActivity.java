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

import butterknife.BindView;
import fr.xgouchet.packageexplorer.R;
import fr.xgouchet.packageexplorer.model.App;
import fr.xgouchet.packageexplorer.ui.fragments.AppDetailFragment;

import static butterknife.ButterKnife.bind;

public class AppDetailsActivity extends AppCompatActivity {

    public static final String EXTRA_PACKAGE_NAME = "package_name";
    private static final String TAG_DETAILS = "details";
    private App app;

    public static void startWithApp(@NonNull Context context, @NonNull App app) {
        Intent intent = buildIntent(context, app);
        context.startActivity(intent);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static void startWithAppAndTransition(@NonNull Activity activity, @NonNull App app, @NonNull View icon) {
        Intent intent = buildIntent(activity, app);
        icon.setTransitionName("icon");
        ActivityOptionsCompat options = ActivityOptionsCompat.
                makeSceneTransitionAnimation(activity, icon, "icon");
        activity.startActivity(intent, options.toBundle());
    }

    @NonNull
    private static Intent buildIntent(@NonNull Context context, @NonNull App app) {
        Intent intent = new Intent(context, AppDetailsActivity.class);
        intent.putExtra("package_name", app.getPackageName());
        return intent;
    }

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.icon_app)
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

        // set content
        setContentView(R.layout.activity_app_details);
        bind(this);

        // action bar
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setSubtitle(app.getPackageName());
        }
        icon.setImageDrawable(app.getIcon());
        setTitle(app.getName());


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
            app = App.fromPackageName(this, packageName);
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
