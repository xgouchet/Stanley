package fr.xgouchet.packageexplorer.ui.callbacks;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.util.SortedListAdapterCallback;
import android.text.TextUtils;

import fr.xgouchet.packageexplorer.core.Preferences;
import fr.xgouchet.packageexplorer.model.App;

/**
 * @author Xavier Gouchet
 */
public abstract class AppSortedListCallback extends SortedListAdapterCallback<App> {


    @NonNull
    public static SortedListAdapterCallback<App> getCallback(@NonNull Context context,
                                                             @NonNull RecyclerView.Adapter adapter) {
        String sort = Preferences.getSort(context);
        switch (sort) {
            case Preferences.SORT_PACKAGE_NAME:
                return new AppSortedListCallback.ByPackageName(adapter);
            case Preferences.SORT_INSTALL_TIME:
                return new AppSortedListCallback.ByInstallTime(adapter);
            case Preferences.SORT_NAME:
            default:
                return new AppSortedListCallback.ByName(adapter);
        }
    }

    public static class ByPackageName extends AppSortedListCallback {

        public ByPackageName(@NonNull RecyclerView.Adapter adapter) {
            super(adapter);
        }

        @Override
        public int compare(App app1, App app2) {
            return app1.getPackageName().compareToIgnoreCase(app2.getPackageName());
        }
    }

    public static class ByName extends AppSortedListCallback {

        public ByName(@NonNull RecyclerView.Adapter adapter) {
            super(adapter);
        }

        @Override
        public int compare(App app1, App app2) {
            return app1.getName().compareToIgnoreCase(app2.getName());
        }
    }

    public static class ByInstallTime extends AppSortedListCallback {

        public ByInstallTime(@NonNull RecyclerView.Adapter adapter) {
            super(adapter);
        }

        @Override
        public int compare(App app1, App app2) {
            // 0 if lhs = rhs, less than 0 if lhs < rhs, and greater than 0 if lhs > rhs.
            long lhs = app1.getInstallTime();
            long rhs = app2.getInstallTime();

            if (lhs == rhs)
                return 0;
            else if (lhs > rhs)
                return -1;
            else
                return 1;
        }
    }

    private AppSortedListCallback(@NonNull RecyclerView.Adapter adapter) {
        super(adapter);
    }

    @Override
    public boolean areContentsTheSame(App oldApp, App newApp) {
        if (!TextUtils.equals(oldApp.getName(), newApp.getName()))
            return false;

        if (oldApp.getFlags() != newApp.getFlags())
            return false;

        // TODO ? compare icon drawables ?
        return true;
    }

    @Override
    public boolean areItemsTheSame(App app1, App app2) {
        return TextUtils.equals(app1.getPackageName(), app2.getPackageName());
    }
}
