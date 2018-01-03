package fr.xgouchet.packageexplorer.details.app

import android.annotation.TargetApi
import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.v4.app.ActivityOptionsCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import fr.xgouchet.packageexplorer.R
import fr.xgouchet.packageexplorer.applist.AppViewModel
import fr.xgouchet.packageexplorer.core.utils.Cutelry.knife
import kotlin.properties.Delegates.notNull

/**
 * @author Xavier F. Gouchet
 */
class AppDetailsActivity : AppCompatActivity() {

    companion object {

        val EXTRA_PACKAGE_NAME = "package_name"

        fun startWithApp(activity: Activity, app: AppViewModel) {
            val intent = buildIntent(activity, app)
            activity.startActivity(intent)
        }

        fun buildIntent(activity: Activity, app: AppViewModel): Intent {
            val intent = Intent(activity, AppDetailsActivity::class.java)
            intent.putExtra(EXTRA_PACKAGE_NAME, app.packageName)
            return intent
        }
    }

    internal var app: AppViewModel? = null
    internal val icon: ImageView by knife(R.id.icon_app)
    internal val packageName: TextView by knife(R.id.text_package_name)

    internal var appDetailsPresenter: AppDetailsPresenter by notNull()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_app_details)

        readAppFromIntent()
        if (isFinishing) return

        setupHeader()

        setupMVP()
    }

    private fun readAppFromIntent() {
        val intent = intent

        val packageName = intent.getStringExtra(EXTRA_PACKAGE_NAME)
        if (packageName.isNullOrBlank()) {
            finish()
            return
        }

        app = AppViewModel.fromPackageName(this, packageName)
    }

    private fun setupHeader() {
        val toolbar = findViewById(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)

        app?.let {
            title = it.title
            toolbar.subtitle = it.packageName
            toolbar.navigationIcon = it.icon
        }

    }

    private fun setupMVP() {
        val fragment = supportFragmentManager.findFragmentById(R.id.fragment_container) as AppDetailsFragmentBase

        appDetailsPresenter = getExistingPresenter(fragment) ?: AppDetailsPresenter(fragment, this, app?.packageName ?: "", app?.isSystemApp == true)
    }

    private fun getExistingPresenter(fragment: AppDetailsFragmentBase): AppDetailsPresenter? {
        val lastPresenter = lastCustomNonConfigurationInstance as AppDetailsPresenter? ?: return null

        lastPresenter.displayer = fragment
        return lastPresenter
    }

}