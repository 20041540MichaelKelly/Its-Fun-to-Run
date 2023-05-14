package mick.studio.itsfuntorun.ui.runlist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseUser
import mick.studio.itsfuntorun.firebase.FirebaseDBManager
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

    fun loadAllFriendsRuns() {
        try {
            //readOnly.value = true
           // FirebaseDBManager.findAllRunsForFriends(liveFirebaseUser.value.toString(),runsList)
            FirebaseDBManager.findAllRuns(runsList)
            Timber.i("Report LoadAll Success : ${runsList.value.toString()}")
        }
        catch (e: Exception) {
            Timber.i("Report LoadAll Error : $e.message")
        }
    }

    fun deleteItem(firebaseUser: MutableLiveData<FirebaseUser>,run: String){
        try
        {
            FirebaseDBManager.delete(firebaseUser.value!!.uid, run)
            Timber.i("deleted : ${run}")

        } catch (e: IllegalArgumentException) {
            Timber.i("Retrofit Error : $e.message")
        }
    }
}