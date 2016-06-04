package fr.xgouchet.packageexplorer.ui.fragments;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.BindView;
import butterknife.ButterKnife;
import fr.xgouchet.packageexplorer.R;
import fr.xgouchet.packageexplorer.model.Apps;
import fr.xgouchet.packageexplorer.ui.adapters.AppInfosAdapter;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.schedulers.Schedulers;

/**
 * @author Xavier Gouchet
 */
public class AppDetailFragment extends Fragment {

    private static final String ARG_PACKAGE_NAME = "package_name";

    @BindView(R.id.app_list)
    RecyclerView recyclerView;

    private final AppInfosAdapter appInfosAdapter = new AppInfosAdapter();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();
        if (args == null) {
            return; // TODO ?
        }

        String packageName = args.getString(ARG_PACKAGE_NAME);
        if (packageName == null) {
            return;
        }

        Observable.create(new Apps.Infos(getActivity(), packageName))
                .subscribeOn(Schedulers.io())
                .onBackpressureBuffer()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(appInfosAdapter);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_list, container, false);
        ButterKnife.bind(this, root);

        recyclerView.setAdapter(appInfosAdapter);
        recyclerView.setNestedScrollingEnabled(false);

        return root;
    }

    public static AppDetailFragment forApp(@NonNull String packageName) {
        AppDetailFragment fragment = new AppDetailFragment();

        Bundle args = new Bundle(1);
        args.putString(ARG_PACKAGE_NAME, packageName);

        fragment.setArguments(args);
        return fragment;
    }
}
