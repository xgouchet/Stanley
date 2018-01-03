package fr.xgouchet.packageexplorer

import android.app.Application
import android.support.v7.app.AppCompatDelegate
import com.facebook.stetho.Stetho
import com.squareup.leakcanary.LeakCanary
import com.tspoon.traceur.Traceur
import timber.log.Timber

class StanleyApplication : Application() {

    override fun onCreate() {
        super.onCreate()


        if (BuildConfig.DEBUG) run {
            Stetho.initializeWithDefaults(this)
            Traceur.enableLogging();
            LeakCanary.install(this)
            Timber.plant(Timber.DebugTree())
        }

        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
    }
}