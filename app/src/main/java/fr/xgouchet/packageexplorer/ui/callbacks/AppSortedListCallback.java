package fr.xgouchet.packageexplorer.ui.callbacks;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.util.SortedListAdapterCallback;
import android.text.TextUtils;

import fr.xgouchet.packageexplorer.model.App;

/**
 * @author Xavier Gouchet
 */
public abstract class AppSortedListCallback extends SortedListAdapterCallback<App> {

    /**
     * Creates a {@link SortedList.Callback} that will forward data change events to the provided
     * Adapter.
     *
     * @param adapter The Adapter instance which should receive events from the SortedList.
     */
    public AppSortedListCallback(RecyclerView.Adapter adapter) {
        super(adapter);
    }

    public static class ByPackageName extends AppSortedListCallback {

        /**
         * Creates a {@link SortedList.Callback} that will forward data change events to the provided
         * Adapter.
         *
         * @param adapter The Adapter instance which should receive events from the SortedList.
         */
        public ByPackageName(RecyclerView.Adapter adapter) {
            super(adapter);
        }

        @Override
        public int compare(App app1, App app2) {
            return app1.getPackageName().compareTo(app2.getPackageName());
        }
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
