package com.skillbox.strava

import android.app.Application
import android.app.NotificationManager
import android.os.Build
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import com.kirich1409.androidnotificationdsl.channels.createNotificationChannels
import com.skillbox.core.notification.NotificationChannels
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber
import javax.inject.Inject

@HiltAndroidApp
class App : Application(), Configuration.Provider {
    @Inject
    lateinit var workerFactory: HiltWorkerFactory
    private var _application: App? = null
    private val application get() = checkNotNull(_application) { "Application isn`n initialized" }

    override fun onCreate() {
        super.onCreate()
        _application = this
        initLog()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannels.createNotificationChannel(this)
        }
    }

    private fun initLog() {
        if (BuildConfig.DEBUG)
            Timber.plant(Timber.DebugTree())
    }

    fun getInstance(): App {
        return application // Учитывая, что нигде не используется наверное можно удалить все, что с этим связано
    }

    override fun getWorkManagerConfiguration() =
            Configuration.Builder()
                    .setWorkerFactory(workerFactory)
                    .build()
}