package fr.xgouchet.packageexplorer.ui.activities;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import fr.xgouchet.packageexplorer.R;
import fr.xgouchet.packageexplorer.applist.AppViewModel;
import fr.xgouchet.packageexplorer.ui.events.AppSelectedEvent;

/**
 * @author Xavier Gouchet
 */
public class AppListActivity extends AppCompatActivity {

    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_app_list);

        setSupportActionBar(toolbar);
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    public void onEvent(AppSelectedEvent event) {
        switchToDetailView(event.getApp(), event.getIcon());
    }

    public void onFabClicked(View view) {
        startActivity(new Intent(getApplicationContext(), AppFiltersActivity.class));
    }

    public void switchToDetailView(@NonNull AppViewModel app, @Nullable View icon) {
        if ((Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                && (icon != null)) {
            OldAppDetailsActivity.startWithAppAndTransition(this, app, icon);
        } else {
            OldAppDetailsActivity.startWithApp(this, app);
        }
    }

}
