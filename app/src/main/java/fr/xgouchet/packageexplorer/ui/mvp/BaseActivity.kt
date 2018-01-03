package fr.xgouchet.packageexplorer.ui.mvp

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import fr.xgouchet.packageexplorer.R
import timber.log.Timber

abstract class BaseActivity<I, T, out P, out D>
    : AppCompatActivity()
        where P : Presenter<T>,
              D : Displayer<T>, D : Fragment {


    protected lateinit var fragment: Displayer<T>
    protected lateinit var presenter: Presenter<T>

    protected lateinit var toolbar: Toolbar

    protected var intentData: I? = null
    private var isRestored: Boolean = false

    // region Activity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_base)
        setupHeader()

        intent?.let {
            intentData = readIntent(it)
        }

        if (savedInstanceState != null) {
            isRestored = true
            presenter = restorePresenter(savedInstanceState)
            @Suppress("UNCHECKED_CAST")
            fragment = supportFragmentManager.findFragmentById(R.id.fragment_container) as D
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

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        val key = getPresenterKey()
        PresenterCache.savePresenter(key, presenter)
        outState?.putString(PresenterCache.PRESENTER_KEY, key)
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

    abstract fun readIntent(intent: Intent): I?

    abstract fun instantiatePresenter(): P

    abstract fun instantiateFragment(): D

    abstract fun getPresenterKey(): String

    // endregion

    // region Internal

    private fun setupHeader() {
        toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
    }


    private fun restorePresenter(savedInstanceState: Bundle): P {
        val key = savedInstanceState.getString(PresenterCache.PRESENTER_KEY, null)

        if (key == null) {
            Timber.w("Expected to restore presenter, but not Key found")
            return instantiatePresenter()
        }

        return PresenterCache.getPresenter(key, { instantiatePresenter() })
    }

    // endregion

}