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
        i("Run started")
       /* runs.add(RunModel("5", "56:00:23"))
        runs.add(RunModel("3.23", "43:02:12"))
        runs.add(RunModel("4.56", "40:01:45"))*/
    }
}
