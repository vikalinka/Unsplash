package lt.vitalikas.unsplash

import android.app.Application
import android.content.pm.ApplicationInfo
import android.os.StrictMode
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber
import javax.inject.Inject

@HiltAndroidApp
class App : Application(), Configuration.Provider {

    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    override fun getWorkManagerConfiguration() =
        Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()

    override fun onCreate() {
        super.onCreate()
        initTimber()
        detectLongOperations()
//        Database.init(this)
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