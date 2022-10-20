package fr.xgouchet.packageexplorer

import android.app.Application
import android.os.StrictMode
import androidx.appcompat.app.AppCompatDelegate
import timber.log.Timber

class StanleyApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) run {
            Class.forName("dalvik.system.CloseGuard")
                .getMethod("setEnabled", Boolean::class.javaPrimitiveType).invoke(null, true)
            StrictMode.setVmPolicy(
                StrictMode.VmPolicy.Builder(StrictMode.getVmPolicy()).detectAll().build()
            )
            Timber.plant(Timber.DebugTree())
        }

        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
    }
}
