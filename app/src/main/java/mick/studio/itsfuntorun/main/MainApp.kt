package mick.studio.itsfuntorun.main

import android.app.Application
import mick.studio.itsfuntorun.models.RunModel
import timber.log.Timber
import timber.log.Timber.i

class MainApp : Application() {
    val runs = ArrayList<RunModel>()

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        i("Placemark started")
        runs.add(RunModel("One", "About one..."))
        runs.add(RunModel("Two", "About two..."))
        runs.add(RunModel("Three", "About three..."))
    }
}
