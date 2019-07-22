package fr.xgouchet.packageexplorer.ui.mvp

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import fr.xgouchet.packageexplorer.R
import timber.log.Timber


abstract class BaseActivity<IntentDataI, DataD, out PresenterP, out DisplayerD>
    : AppCompatActivity()
        where IntentDataI : Any,
              PresenterP : Presenter<DataD>,
              DisplayerD : Displayer<DataD>, DisplayerD : Fragment {


    abstract val allowNullIntentData: Boolean

    protected lateinit var fragment: Displayer<DataD>
    protected lateinit var presenter: Presenter<DataD>

    protected lateinit var toolbar: Toolbar

    protected var intentData: IntentDataI? = null
    private var isRestored: Boolean = false

    // region Activity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_base)
        setupHeader()

        val data: IntentDataI? = readIntent(intent)
        if (data == null && !allowNullIntentData) {
            Timber.w("Intent data is null, aborting activity")
            finish()
            return
        }

        intentData = data

        if (savedInstanceState != null) {
            isRestored = true
            presenter = restorePresenter(savedInstanceState)
            @Suppress("UNCHECKED_CAST")
            fragment = supportFragmentManager.findFragmentById(R.id.fragment_container) as DisplayerD
        } else {
            presenter = instantiatePresenter()
            val displayer = instantiateFragment()
            supportFragmentManager.beginTransaction()
                    .add(R.id.fragment_container, displayer)
                    .commit()
            fragment = displayer
        }
    }

    override fun onRestart() {
        super.onRestart()
        isRestored = true
    }

    override fun onStart() {
        super.onStart()
        presenter.onDisplayerAttached(fragment, isRestored)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        super.onRestoreInstanceState(savedInstanceState)

        if (savedInstanceState != null) {
            if (isRestored) {
                Timber.w("Already restored but restoring again ‽")
                return
            }
            isRestored = true
            val restoredPresenter = restorePresenter(savedInstanceState)
            if (restoredPresenter !== presenter) {
                presenter.onDisplayerDetached()
                presenter = restoredPresenter
                presenter.onDisplayerAttached(fragment, isRestored)
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        val key = getPresenterKey()
        PresenterCache.savePresenter(key, presenter)
        outState.putString(PresenterCache.PRESENTER_KEY, key)
    }


    override fun onStop() {
        super.onStop()
        presenter.onDisplayerDetached()

        if (isFinishing) {
            val key = getPresenterKey()
            PresenterCache.dropPresenter(key)
        }
    }

    // endregion

    // region Abstract

    abstract fun readIntent(intent: Intent): IntentDataI?

    abstract fun instantiatePresenter(): PresenterP

    abstract fun instantiateFragment(): DisplayerD

    abstract fun getPresenterKey(): String

    // endregion

    // region Internal

    private fun setupHeader() {
        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
    }


    private fun restorePresenter(savedInstanceState: Bundle): PresenterP {
        val key = savedInstanceState.getString(PresenterCache.PRESENTER_KEY, null)
        val factory = { instantiatePresenter() }

        if (key == null) {
            Timber.w("Expected to restore presenter, but not Key found")
            return factory.invoke()
        }

        return PresenterCache.getPresenter(key, factory)
    }

    // endregion

}
