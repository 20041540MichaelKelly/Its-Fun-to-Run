package mick.studio.itsfuntorun.ui.details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import mick.studio.itsfuntorun.firebase.FirebaseDBManager
import mick.studio.itsfuntorun.models.RunModel
import timber.log.Timber

class RunDetailViewModel : ViewModel() {
    private val run = MutableLiveData<RunModel>()

    var observableRun: LiveData<RunModel>
        get() = run
        set(value) {run.value = value.value}

    fun getRun(userid:String, runid: String) {
        try {
            FirebaseDBManager.findById(userid, runid, run)
            Timber.i("Detail getRun() Success : ${
                run.value.toString()}")
        }
        catch (e: Exception) {
            Timber.i("Detail getRun() Error : $e.message")
        }
    }

    fun updateRun(userid:String, id: String,run: RunModel) {
        try {
            FirebaseDBManager.updateRun(userid, id, run)
            Timber.i("Detail update() Success : $run")
        }
        catch (e: Exception) {
            Timber.i("Detail update() Error : $e.message")
        }
    }
}