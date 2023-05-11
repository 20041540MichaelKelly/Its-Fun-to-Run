package mick.studio.itsfuntorun.ui.analysis

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseUser
import mick.studio.itsfuntorun.firebase.FirebaseDBManager
import mick.studio.itsfuntorun.models.RunModel
import timber.log.Timber

class AnalysisViewModel(app: Application) : AndroidViewModel(app) {
    private val runsList = MutableLiveData<List<RunModel>>()

    val observableRunsList: LiveData<List<RunModel>>
        get() = runsList

    var liveFirebaseUser = MutableLiveData<FirebaseUser>()
    private val _runModel = MutableLiveData<RunModel>()

    val observableRunModel: LiveData<RunModel>
        get() = _runModel


    fun load() {
        try {
            //val userid = liveFirebaseUser.value?.uid!!
            FirebaseDBManager.findAll(liveFirebaseUser.value?.uid!!, runsList)
            Timber.i("Run List Load Success : ${runsList.value.toString()}")

        }
        catch (e: Exception) {
            Timber.i("ViewModel Error : $e.message")
        }
    }
}
