//package fr.xgouchet.packageexplorer.ui.adapters;
//
//import android.os.Build;
//import android.support.annotation.DrawableRes;
//import android.support.annotation.NonNull;
//import android.support.annotation.Nullable;
//import android.support.annotation.StringRes;
//import android.support.v7.widget.RecyclerView;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import fr.xgouchet.packageexplorer.R;
//import fr.xgouchet.packageexplorer.model.AppInfo;
//import rx.Observer;
//
///**
// * @author Xavier Gouchet
// */
//public class AppInfosAdapter extends RecyclerView.Adapter<AppInfosAdapter.ViewHolder>
//        implements Observer<AppInfo> {
//
//
//    public static final int VIEW_TYPE_HEADER = 1;
//    public static final int VIEW_TYPE_INFO = 2;
//    public static final int VIEW_TYPE_ACTIVITY = 3;
//    public static final int VIEW_TYPE_PERMISSIONS = 4;
//
//    private final List<AppInfo>[] infos;
//
//    public AppInfosAdapter() {
//        //noinspection unchecked
//        infos = new List[AppInfo.INFO_TYPES_COUNT];
//        for (int i = 0; i < AppInfo.INFO_TYPES_COUNT; ++i) {
//            infos[i] = new ArrayList<>();
//        }
//    }
//
//    @Override
//    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        int layout = 0;
//        switch (viewType) {
//            case VIEW_TYPE_HEADER:
//                layout = R.layout.item_section_header;
//                break;
//            case VIEW_TYPE_INFO:
//                layout = R.layout.item_info;
//                break;
//            case VIEW_TYPE_ACTIVITY:
//                layout = R.layout.item_activity;
//                break;
//            case VIEW_TYPE_PERMISSIONS:
//                layout = R.layout.item_permission;
//                break;
//        }
//
//
//        View root = LayoutInflater.from(parent.getContext())
//                .inflate(layout, parent, false);
//        return new ViewHolder(root);
//    }
//
//    @Override
//    public void onBindViewHolder(ViewHolder holder, int position) {
//        AppInfo appInfo = getItem(position);
//
//        if (appInfo.isHeader()) {
//            holder.info.setText(appInfo.getInfoRes());
//            int iconRes = getHeaderDrawable(appInfo.getType());
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
//                holder.info.setCompoundDrawablesRelativeWithIntrinsicBounds(iconRes, 0, 0, 0);
//            }
//        } else if (appInfo.getType() == AppInfo.INFO_TYPE_ACTIVITIES) {
//            holder.info.setText(appInfo.getInfo());
//            if (holder.subinfo != null) {
//                holder.subinfo.setText(appInfo.getSubInfo());
//            }
//            if (holder.icon != null) {
//                holder.icon.setImageDrawable(appInfo.getIcon());
//            }
//        } else if (appInfo.getType() == AppInfo.INFO_TYPE_PERMISSIONS) {
//            holder.info.setText(appInfo.getInfo());
//            if (holder.subinfo != null) {
//                holder.subinfo.setText(appInfo.getSubInfo());
//            }
//        } else {
//            holder.info.setText(appInfo.getInfo());
//        }
//    }
//
//    @Override
//    public int getItemCount() {
//        int count = 0;
//        for (int i = 0; i < AppInfo.INFO_TYPES_COUNT; ++i) {
//            if (!infos[i].isEmpty()) {
//                count += infos[i].size();
//            }
//        }
//        return count;
//    }
//
//    @Override
//    public int getItemViewType(int position) {
//        AppInfo appInfo = getItem(position);
//
//        if (appInfo.isHeader()) {
//            return VIEW_TYPE_HEADER;
//        } else if (appInfo.getType() == AppInfo.INFO_TYPE_ACTIVITIES) {
//            return VIEW_TYPE_ACTIVITY;
//        } else if (appInfo.getType() == AppInfo.INFO_TYPE_PERMISSIONS) {
//            return VIEW_TYPE_PERMISSIONS;
//        } else {
//            return VIEW_TYPE_INFO;
//        }
//    }
//
//    @NonNull
//    private AppInfo getItem(int position) {
//        int check = 0;
//        int type = 0;
//
//        AppInfo appInfo = null;
//        while ((position >= check) && (type < AppInfo.INFO_TYPES_COUNT)) {
//            int count = infos[type].size();
//
//            if ((position - check) >= count) {
//                check += count;
//                type++;
//            } else {
//                appInfo = infos[type].get(position - check);
//                break;
//            }
//        }
//
//        if (appInfo == null) {
//            throw new IllegalStateException();
//        }
//
//        return appInfo;
//    }
//
//
//    public void onError(Throwable e) {
//        Log.e("AppInfoAdapter", e.getMessage(), e);
//    }
//
//    public void onNext(AppInfo appInfo) {
//        insert(appInfo);
//    }
//
//    public void onCompleted() {
//
//    }
//
//    private void insert(@NonNull AppInfo appInfo) {
//        int type = appInfo.getType();
//
//        int insertedAt = 0;
//        for (int i = 0; i < type; ++i) {
//            if (!infos[i].isEmpty()) {
//                insertedAt += infos[i].size();
//            }
//        }
//
//        if (infos[type].isEmpty()) {
//            infos[type].add(new AppInfo(type, getHeaderString(type), true));
//            notifyItemInserted(insertedAt);
//            insertedAt++;
//        } else {
//            insertedAt += infos[type].size();
//        }
//
//        infos[type].add(appInfo);
//        notifyItemInserted(insertedAt);
//    }
//
//    @StringRes
//    private int getHeaderString(int type) {
//        switch (type) {
//            case AppInfo.INFO_TYPE_GLOBAL:
//                return R.string.header_type_global;
//            case AppInfo.INFO_TYPE_FEATURES_REQUIRED:
//                return R.string.header_type_features;
//            case AppInfo.INFO_TYPE_CUSTOM_PERMISSIONS:
//                return R.string.header_type_custom_permissions;
//            case AppInfo.INFO_TYPE_PERMISSIONS:
//                return R.string.header_type_uses_permissions;
//            case AppInfo.INFO_TYPE_ACTIVITIES:
//                return R.string.header_type_activities;
//            case AppInfo.INFO_TYPE_SERVICES:
//                return R.string.header_type_services;
//            case AppInfo.INFO_TYPE_PROVIDERS:
//                return R.string.header_type_providers;
//            case AppInfo.INFO_TYPE_RECEIVERS:
//                return R.string.header_type_receivers;
//        }
//        throw new IllegalArgumentException();
//    }
//
//    @DrawableRes
//    private int getHeaderDrawable(int type) {
//        switch (type) {
//            case AppInfo.INFO_TYPE_GLOBAL:
//                return R.drawable.ic_info;
//            case AppInfo.INFO_TYPE_FEATURES_REQUIRED:
//                return R.drawable.ic_feature;
//            case AppInfo.INFO_TYPE_CUSTOM_PERMISSIONS:
//                return R.drawable.ic_custom_permission;
//            case AppInfo.INFO_TYPE_PERMISSIONS:
//                return R.drawable.ic_permission;
//            case AppInfo.INFO_TYPE_ACTIVITIES:
//                return R.drawable.ic_activity;
//            case AppInfo.INFO_TYPE_SERVICES:
//                return R.drawable.ic_services;
//            case AppInfo.INFO_TYPE_PROVIDERS:
//                return R.drawable.ic_provider;
//            case AppInfo.INFO_TYPE_RECEIVERS:
//                return R.drawable.ic_receiver;
//        }
//        throw new IllegalArgumentException();
//    }
//
//    public class ViewHolder extends RecyclerView.ViewHolder {
//
//        TextView info;
//
//        @Nullable
//        TextView subinfo;
//
//        @Nullable
//        ImageView icon;
//
//        public ViewHolder(View itemView) {
//            super(itemView);
//
//        }
//    }
//}
