package fr.xgouchet.packageexplorer.ui.events;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import fr.xgouchet.packageexplorer.model.App;

/**
 * @author Xavier Gouchet
 */
public class AppSelectedEvent {

    @NonNull
    private final App app;
    @Nullable
    private final View icon;

    public AppSelectedEvent(@NonNull App app) {
        this(app, null);
    }

    public AppSelectedEvent(@NonNull App app, @Nullable View icon) {
        this.app = app;
        this.icon = icon;
    }

    @NonNull
    public App getApp() {
        return app;
    }

    @Nullable
    public View getIcon() {
        return icon;
    }
}
