package fr.xgouchet.packageexplorer.ui.activities;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.BindView;
import butterknife.OnClick;
import fr.xgouchet.packageexplorer.R;
import fr.xgouchet.packageexplorer.model.App;
import fr.xgouchet.packageexplorer.ui.events.AppSelectedEvent;

import static butterknife.ButterKnife.bind;

/**
 * @author Xavier Gouchet
 */
public class AppListActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_app_list);
        bind(this);

        setSupportActionBar(toolbar);
    }

    @Override
    protected void onResume() {
        super.onResume();

        EventBus.getDefault().register(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void onEvent(AppSelectedEvent event) {
        switchToDetailView(event.getApp(), event.getIcon());
    }

    @OnClick(R.id.fab)
    public void onFabClicked(View view) {
        startActivity(new Intent(getApplicationContext(), AppFiltersActivity.class));
    }

    public void switchToDetailView(@NonNull App app, @Nullable View icon) {
        if ((Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                && (icon != null)) {
            AppDetailsActivity.startWithAppAndTransition(this, app, icon);
        } else {
            AppDetailsActivity.startWithApp(this, app);
        }
    }

}
