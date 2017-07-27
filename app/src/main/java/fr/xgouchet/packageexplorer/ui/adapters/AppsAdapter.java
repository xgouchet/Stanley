package fr.xgouchet.packageexplorer.ui.adapters;

/**
 * @author Xavier Gouchet
 */
//public class AppsAdapter extends RecyclerView.Adapter<AppsAdapter.ViewHolder> {
//
//
//    private final List<AppViewModel> appList = new ArrayList<>();
//    private final SortedList<AppViewModel> filteredAppList;
//    private final boolean ignoreSystemApps;
//
//    @Nullable
//    private String currentFilter = null;
//
//    public AppsAdapter(@NonNull Context context) {
//        SortedListAdapterCallback<AppViewModel> sortedListCallback = AppSortedListCallback.getCallback(context, this);
//        ignoreSystemApps = !Preferences.getDisplaySystemApp(context);
//        filteredAppList = new SortedList<>(AppViewModel.class, sortedListCallback);
//    }
//
//
//    @Override
//    public AppsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        View root = LayoutInflater.from(parent.getContext())
//                .inflate(R.layout.item_app, parent, false);
//        return new ViewHolder(root);
//    }
//
//    @Override
//    public int getItemCount() {
//        return filteredAppList.size();
//    }
//
//    @Override
//    public void onBindViewHolder(AppsAdapter.ViewHolder holder, int position) {
//        AppViewModel app = filteredAppList.get(position);
//        holder.bind(app);
//    }
//
//    public void clear() {
//        appList.clear();
//        filteredAppList.clear();
//    }
//
//    public void onError(Throwable e) {
//        Log.e("AppsAdapter", e.getMessage(), e);
//    }
//
//    public void onNext(List<AppViewModel> apps) {
//        for (AppViewModel app : apps) {
//            onNextApp(app);
//        }
//    }
//
//    private void onNextApp(AppViewModel app) {
//        if (app == null) return;
//
//        // Ignore system apps ?
//        if (ignoreSystemApps) {
//            if ((app.getFlags() & ApplicationInfo.FLAG_SYSTEM) > 0) {
//                return;
//            }
//        }
//
//        appList.add(app);
//        insert(app);
//    }
//
//    public void onCompleted() {
//        // TODO empty view
//    }
//
//    private void insert(@NonNull AppViewModel app) {
//        filteredAppList.add(app);
//    }
//
//    private void remove(@NonNull AppViewModel app) {
//        filteredAppList.remove(app);
//    }
//
//    public synchronized void updateFilter(@Nullable String filter) {
//        currentFilter = filter;
//
//        removeFiltered();
//        addNonFiltered();
//    }
//
//    private void addNonFiltered() {
//        for (int i = 0, count = appList.size(); i < count; i++) {
//            AppViewModel app = appList.get(i);
//            if (filteredAppList.indexOf(app) == SortedList.INVALID_POSITION) {
//                if (!isFiltered(app)) {
//                    insert(app);
//                }
//            }
//        }
//    }
//
//    private void removeFiltered() {
//        for (int i = filteredAppList.size() - 1; i >= 0; i--) {
//            AppViewModel app = filteredAppList.get(i);
//            if (isFiltered(app)) {
//                remove(app);
//            }
//        }
//    }
//
//    private boolean isFiltered(@NonNull AppViewModel app) {
//        if (TextUtils.isEmpty(currentFilter)) {
//            return false;
//        }
//
//        if (app.getTitle().contains(currentFilter)) {
//            return false;
//        }
//
//        if (app.getPackageName().contains(currentFilter)) {
//            return false;
//        }
//
//        return true;
//    }
//
//    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
//
////        @BindView(R.id.icon_app)
////        ImageView icon;
////
////        @BindView(R.id.text_title)
////        TextView title;
////        @BindView(R.id.text_package_name)
////        TextView subtitle;
////        @BindView(R.id.text_install)
////        TextView install;
////
////        @Nullable
////        private AppViewModel app;
////
////        private static final DateFormat DATE_FORMAT = SimpleDateFormat.getDateInstance();
////
////        public ViewHolder(View itemView) {
////            super(itemView);
////            ButterKnife.bind(this, itemView);
////
////            itemView.setOnClickListener(this);
////        }
////
////        public void bind(AppViewModel app) {
////            this.app = app;
////            title.setText(app.getTitle());
////            subtitle.setText(app.getPackageName());
////            icon.setImageDrawable(app.getIcon());
////            synchronized (DATE_FORMAT) {
////                install.setText(DATE_FORMAT.format(new Date(app.getInstallTime())));
////            }
////        }
////
////        @Override
////        public void onClick(View view) {
////            if (app == null) return;
////
////            Log.i("Click", "App : " + app.getTitle());
////            EventBus.getDefault().post(new AppSelectedEvent(app, icon));
////        }
//    }
//}
