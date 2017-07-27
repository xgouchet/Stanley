package fr.xgouchet.packageexplorer.applist

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import fr.xgouchet.packageexplorer.R
import kotlin.properties.Delegates.notNull

/**
 * @author Xavier F. Gouchet
 */
class AppListActivity : AppCompatActivity() {

    private var appListPresenter: AppListPresenter by notNull()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_app_list)

        setupHeader()

        setupMVP()
    }

    private fun setupHeader() {
        setTitle(R.string.title_activity_app_list)
        val toolbar = findViewById(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)
    }

    private fun setupMVP() {
        val fragment = supportFragmentManager.findFragmentById(R.id.fragment_container) as AppListFragment

        appListPresenter = getExistingPresenter(fragment) ?: AppListPresenter(fragment, this)
        fragment.presenter = appListPresenter
    }

    private fun getExistingPresenter(fragment: AppListFragment): AppListPresenter? {
        val lastPresenter = lastCustomNonConfigurationInstance as AppListPresenter? ?: return null

        lastPresenter.displayer = fragment
        lastPresenter.activity = this
        return lastPresenter
    }

    override fun onRetainCustomNonConfigurationInstance(): Any {
        return appListPresenter
    }
}