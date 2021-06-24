package com.skillbox.strava

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class App : Application() {

    private var _application: App? = null
    private val application get() = checkNotNull(_application) { "Application isn`n initialized" }

    override fun onCreate() {
        super.onCreate()
        _application = this
        initLog()
    }

    private fun initLog() {
        if (BuildConfig.DEBUG)
            Timber.plant(Timber.DebugTree())
    }

    fun getInstance(): App {
        return application
    }
}