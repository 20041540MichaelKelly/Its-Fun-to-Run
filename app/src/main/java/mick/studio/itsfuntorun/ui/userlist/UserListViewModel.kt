package mick.studio.itsfuntorun.ui.userlist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseUser
import mick.studio.itsfuntorun.firebase.FirebaseDBManager
import mick.studio.itsfuntorun.models.RunModel
import mick.studio.itsfuntorun.models.users.UserModel
import timber.log.Timber

class UserListViewModel : ViewModel() {
    private val usersList = MutableLiveData<List<UserModel>>()
    private val user = MutableLiveData<UserModel>()

    val observableUsersList: LiveData<List<UserModel>>
        get() = usersList

    var liveFirebaseUser = MutableLiveData<FirebaseUser>()
    var readOnly = MutableLiveData(false)

    init {
        load()
    }

    fun load() {
        try {
            readOnly.value = false
            //val userid = liveFirebaseUser.value?.uid!!
            FirebaseDBManager.findAllUsers(liveFirebaseUser.value?.uid!!, usersList)
            Timber.i("User List Load Success : ${usersList.value.toString()}")

        }
        catch (e: Exception) {
            Timber.i("Loading Error : $e.message")
        }
    }

    fun loadAll() {
        try {
            readOnly.value = true
            FirebaseDBManager.findAllUsers(liveFirebaseUser.value?.uid!!,usersList)
            Timber.i("Users Loaded Success : ${usersList.value.toString()}")
        }
        catch (e: Exception) {
            Timber.i("Users LoadAll Error : $e.message")
        }
    }
}