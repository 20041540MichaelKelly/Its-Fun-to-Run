package mick.studio.itsfuntorun.activities.main

import android.app.Application
import mick.studio.itsfuntorun.activities.models.RunModel
import timber.log.Timber
import timber.log.Timber.i

class MainApp : Application() {
    val runs = ArrayList<RunModel>()

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        i("Placemark started")
    }
}
