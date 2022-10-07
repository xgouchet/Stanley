package fr.xgouchet.packageexplorer

import android.app.Application
import android.os.StrictMode
import androidx.appcompat.app.AppCompatDelegate
import timber.log.Timber

class StanleyApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) run {
//            Stetho.initializeWithDefaults(this)
//            Traceur.enableLogging();
//            LeakCanary.install(this)
            try {
                Class.forName("dalvik.system.CloseGuard")
                    .getMethod("setEnabled", Boolean::class.javaPrimitiveType)
                    .invoke(null, true)
            } catch (e: ReflectiveOperationException) {
                throw RuntimeException(e)
            }
            StrictMode.setVmPolicy(
                StrictMode.VmPolicy.Builder(StrictMode.getVmPolicy())
                    .detectLeakedClosableObjects()
                    .build()
            )
            Timber.plant(Timber.DebugTree())
        }

        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
    }
}
