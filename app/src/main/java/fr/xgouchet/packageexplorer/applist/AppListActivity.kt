package fr.xgouchet.packageexplorer.applist

import android.content.Intent
import android.os.Bundle
import fr.xgouchet.packageexplorer.R
import fr.xgouchet.packageexplorer.ui.mvp.BaseActivity

class AppListActivity
    : BaseActivity<Any?, List<AppViewModel>, AppListPresenter, AppListFragment>() {

    override fun readIntent(intent: Intent): Any? {
        return null
    }

    override fun instantiatePresenter(): AppListPresenter {
        return AppListPresenter(null, this)
    }

    override fun instantiateFragment(): AppListFragment {
        return AppListFragment()
    }

    override fun getPresenterKey(): String {
        return "app_list"
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        setTitle(R.string.activity_title_appList)
    }

//
//    private var appListPresenter: AppListPresenter by notNull()
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//
//        setContentView(R.layout.activity_app_list)
//
//        setupHeader()
//
//        setupMVP()
//    }
//
//    private fun setupHeader() {
//        setTitle(R.string.title_activity_app_list)
//        val toolbar = findViewById(R.id.toolbar) as Toolbar
//        setSupportActionBar(toolbar)
//    }
//
//    private fun setupMVP() {
//        val fragment = supportFragmentManager.findFragmentById(R.id.fragment_container) as AppListFragment
//
//        appListPresenter = getExistingPresenter(fragment) ?: AppListPresenter(fragment, this)
//        fragment.presenter = appListPresenter
//    }
//
//    private fun getExistingPresenter(fragment: AppListFragment): AppListPresenter? {
//        val lastPresenter = lastCustomNonConfigurationInstance as AppListPresenter? ?: return null
//
//        lastPresenter.activity = this
//        return lastPresenter
//    }
//
//    override fun onRetainCustomNonConfigurationInstance(): Any {
//        return appListPresenter
//    }
}