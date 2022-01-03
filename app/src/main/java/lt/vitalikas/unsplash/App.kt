package lt.vitalikas.unsplash

import android.app.Application
import android.content.pm.ApplicationInfo
import android.os.StrictMode
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class App : Application() {

    override fun onCreate() {
        super.onCreate()
        initTimber()
        detectLongOperations()
    }

    private fun initTimber() {
        Timber.plant(Timber.DebugTree())
    }

    private fun detectLongOperations() {
        if (applicationContext.applicationInfo.flags != 0 && ApplicationInfo.FLAG_DEBUGGABLE != 0) {
            StrictMode.ThreadPolicy.Builder()
                .detectDiskReads()
                .detectDiskWrites()
                .detectNetwork()
                .penaltyDeath()
                .build()
        }
    }
}