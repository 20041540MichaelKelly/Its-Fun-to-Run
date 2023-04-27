package mick.studio.itsfuntorun.ui.runlist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseUser
import mick.studio.itsfuntorun.firebase.FirebaseDBManager
import mick.studio.itsfuntorun.models.RunManager
import mick.studio.itsfuntorun.models.RunModel
import timber.log.Timber

class RunListViewModel : ViewModel() {
    private val runsList = MutableLiveData<List<RunModel>>()

    val observableRunsList: LiveData<List<RunModel>>
        get() = runsList

    var liveFirebaseUser = MutableLiveData<FirebaseUser>()
    var readOnly = MutableLiveData(false)

    init {
        load()
    }


    fun load() {
        try {
            readOnly.value = false
            //val userid = liveFirebaseUser.value?.uid!!
            FirebaseDBManager.findAll(liveFirebaseUser.value?.uid!!, runsList)
            Timber.i("Run List Load Success : ${runsList.value.toString()}")

        }
        catch (e: Exception) {
            Timber.i("Retrofit Error : $e.message")
        }
    }

    fun loadAll() {
        try {
            readOnly.value = true
            FirebaseDBManager.findAll(runsList)
            Timber.i("Report LoadAll Success : ${runsList.value.toString()}")
        }
        catch (e: Exception) {
            Timber.i("Report LoadAll Error : $e.message")
        }
    }
}