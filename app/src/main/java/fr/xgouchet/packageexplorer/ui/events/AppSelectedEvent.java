package fr.xgouchet.packageexplorer.ui.events;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import fr.xgouchet.packageexplorer.applist.AppViewModel;

/**
 * @author Xavier Gouchet
 */
public class AppSelectedEvent {

    @NonNull
    private final AppViewModel app;
    @Nullable
    private final View icon;

    public AppSelectedEvent(@NonNull AppViewModel app) {
        this(app, null);
    }

    public AppSelectedEvent(@NonNull AppViewModel app, @Nullable View icon) {
        this.app = app;
        this.icon = icon;
    }

    @NonNull
    public AppViewModel getApp() {
        return app;
    }

    @Nullable
    public View getIcon() {
        return icon;
    }
}
