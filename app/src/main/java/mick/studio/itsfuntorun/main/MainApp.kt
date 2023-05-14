package mick.studio.itsfuntorun.main

import android.app.Application
import mick.studio.itsfuntorun.models.RunManager
import timber.log.Timber
import timber.log.Timber.i

class MainApp : Application() {
    //val runs = RunManager()

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        i("Run started")
    }
}
