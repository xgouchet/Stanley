package fr.xgouchet.packageexplorer.ui.adapters;

import android.content.pm.ApplicationInfo;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.util.SortedList;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import fr.xgouchet.packageexplorer.R;
import fr.xgouchet.packageexplorer.model.App;
import fr.xgouchet.packageexplorer.ui.callbacks.AppSortedListCallback;
import fr.xgouchet.packageexplorer.ui.events.AppSelectedEvent;
import rx.Observer;

/**
 * @author Xavier Gouchet
 */
public class AppsAdapter extends RecyclerView.Adapter<AppsAdapter.ViewHolder>
        implements Observer<List<App>> {


    private final List<App> appList = new ArrayList<>();
    private final SortedList<App> filteredAppList;

    @Nullable
    private String currentFilter = null;

    public AppsAdapter() {
        filteredAppList = new SortedList<>(App.class, new AppSortedListCallback.ByPackageName(this));
    }

    @Override
    public AppsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View root = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_app, parent, false);
        return new ViewHolder(root);
    }

    @Override
    public int getItemCount() {
        return filteredAppList.size();
    }

    @Override
    public void onBindViewHolder(AppsAdapter.ViewHolder holder, int position) {
        App app = filteredAppList.get(position);
        holder.bind(app);
    }

    public void clear() {
        appList.clear();
        filteredAppList.clear();
    }

    @Override
    public void onError(Throwable e) {
        Log.e("AppsAdapter", e.getMessage(), e);
    }

    @Override
    public void onNext(List<App> apps) {
        for (App app : apps) {
            onNextApp(app);
        }
    }

    private void onNextApp(App app) {
        if (app == null) return;

        // Ignore system apps
        if ((app.getFlags() & ApplicationInfo.FLAG_SYSTEM) > 0) {
            return;
        }

        appList.add(app);
        insert(app);
    }

    @Override
    public void onCompleted() {
        // TODO empty view
    }

    private void insert(@NonNull App app) {
        filteredAppList.add(app);
    }

    private void remove(@NonNull App app) {
        filteredAppList.remove(app);
    }

    public synchronized void updateFilter(@Nullable String filter) {
        currentFilter = filter;

        removeFiltered();
        addNonFiltered();
    }

    private void addNonFiltered() {
        for (int i = 0, count = appList.size(); i < count; i++) {
            App app = appList.get(i);
            if (filteredAppList.indexOf(app) == SortedList.INVALID_POSITION) {
                if (!isFiltered(app)) {
                    insert(app);
                }
            }
        }
    }

    private void removeFiltered() {
        for (int i = filteredAppList.size() - 1; i >= 0; i--) {
            App app = filteredAppList.get(i);
            if (isFiltered(app)) {
                remove(app);
            }
        }
    }

    private boolean isFiltered(@NonNull App app) {
        if (TextUtils.isEmpty(currentFilter)) {
            return false;
        }

        if (app.getName().contains(currentFilter)) {
            return false;
        }

        if (app.getPackageName().contains(currentFilter)) {
            return false;
        }

        return true;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.icon_app)
        ImageView icon;

        @BindView(R.id.text_title)
        TextView title;
        @BindView(R.id.text_subtitle)
        TextView subtitle;

        @Nullable
        private App app;


        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            itemView.setOnClickListener(this);
        }

        public void bind(App app) {
            this.app = app;
            title.setText(app.getName());
            subtitle.setText(app.getPackageName());
            icon.setImageDrawable(app.getIcon());
        }

        @Override
        public void onClick(View view) {
            if (app == null) return;

            Log.i("Click", "App : " + app.getName());
            EventBus.getDefault().post(new AppSelectedEvent(app, icon));
        }
    }
}
