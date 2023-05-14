package mick.studio.itsfuntorun.ui.userlist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseUser
import mick.studio.itsfuntorun.firebase.FirebaseDBManager
import mick.studio.itsfuntorun.models.RunModel
import mick.studio.itsfuntorun.models.friends.FriendsModel
import mick.studio.itsfuntorun.models.users.UserModel
import timber.log.Timber

class UserListViewModel : ViewModel() {
    private val usersList = MutableLiveData<List<UserModel>>()
    private val friendsList = MutableLiveData<List<FriendsModel>>()
    private val user = MutableLiveData<UserModel>()
    private val _name = MutableLiveData<String>()

    val observableUsersList: LiveData<List<UserModel>>
        get() = usersList

    val observableName: LiveData<String>
        get() = _name

    val observableFriends: LiveData<List<FriendsModel>>
        get() = friendsList

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

    fun getName() {
        try {
            readOnly.value = false
            //val userid = liveFirebaseUser.value?.uid!!
            FirebaseDBManager.getName(liveFirebaseUser.value?.uid!!, _name)
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