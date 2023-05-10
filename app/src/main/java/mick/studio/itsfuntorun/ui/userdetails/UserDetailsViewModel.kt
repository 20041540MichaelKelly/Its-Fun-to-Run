package mick.studio.itsfuntorun.ui.userdetails

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseUser
import mick.studio.itsfuntorun.firebase.FirebaseDBManager
import mick.studio.itsfuntorun.models.RunModel
import mick.studio.itsfuntorun.models.friends.FriendsModel
import mick.studio.itsfuntorun.models.users.UserModel
import timber.log.Timber

class UserDetailsViewModel : ViewModel() {
    private val user = MutableLiveData<UserModel>()
    private val friend = MutableLiveData<FriendsModel>()

    var observableUser: LiveData<UserModel>
        get() = user
        set(value) {user.value = value.value}

    fun getUser(userid:String) {
        try {
            FirebaseDBManager.findUser(userid, user)
            Timber.i("Detail getUser() Success : ${
                user.value.toString()}")
        }
        catch (e: Exception) {
            Timber.i("Detail getUser() Error : $e.message")
        }
    }

    fun addFriend(friend: FriendsModel, firebaseUser: MutableLiveData<FirebaseUser>) {
        try {
            FirebaseDBManager.addFriend(firebaseUser, friend)
            Timber.i(
                "Detail addFriend() Success : ${
                    friend.fid.toString()
                }"
            )
        } catch (e: Exception) {
            Timber.i("Detail addFriend() Error : $e.message")
        }
    }

    fun updateUserFriends(user: UserModel) {
        try {
           FirebaseDBManager.updateUserFriends(user)
            Timber.i("Detail update() Success : $user")
        }
        catch (e: Exception) {
            Timber.i("Detail update() Error : $e.message")
        }
    }
}