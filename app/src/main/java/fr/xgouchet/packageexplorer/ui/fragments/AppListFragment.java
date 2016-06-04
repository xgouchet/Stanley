package fr.xgouchet.packageexplorer.ui.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import butterknife.BindView;
import butterknife.ButterKnife;
import fr.xgouchet.packageexplorer.R;
import fr.xgouchet.packageexplorer.model.Apps;
import fr.xgouchet.packageexplorer.ui.adapters.AppsAdapter;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * @author Xavier Gouchet
 */
public class AppListFragment extends Fragment implements SearchView.OnQueryTextListener {

    @BindView(R.id.app_list)
    RecyclerView recyclerView;

    private final AppsAdapter appsAdapter = new AppsAdapter();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        appsAdapter.clear();

        Observable.create(new Apps.List(getActivity()))
                .subscribeOn(Schedulers.io())
                .buffer(32)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(appsAdapter);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_list, container, false);
        ButterKnife.bind(this, root);

        recyclerView.setAdapter(appsAdapter);

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        inflater.inflate(R.menu.search_menu, menu);

        final MenuItem item = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
        searchView.setOnQueryTextListener(this);
    }


    @Override
    public boolean onQueryTextSubmit(String query) {

        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        if (newText == null) {
            appsAdapter.updateFilter("");
        } else {
            appsAdapter.updateFilter(newText);
        }
        return true;
    }

}
