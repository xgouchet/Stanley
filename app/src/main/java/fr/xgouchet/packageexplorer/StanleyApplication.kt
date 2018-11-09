package fr.xgouchet.packageexplorer

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import timber.log.Timber

class StanleyApplication : Application() {

    override fun onCreate() {
        super.onCreate()


        if (BuildConfig.DEBUG) run {
//            Stetho.initializeWithDefaults(this)
//            Traceur.enableLogging();
//            LeakCanary.install(this)
            Timber.plant(Timber.DebugTree())
        }

        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
    }
}