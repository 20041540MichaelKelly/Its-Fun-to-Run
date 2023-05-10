package mick.studio.itsfuntorun.models.users

import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseUser
import mick.studio.itsfuntorun.models.RunModel

interface UserStore {
    fun createUser(user: UserModel)

    fun findAllUsers(
        userid: String,
        usersList:
        MutableLiveData<List<UserModel>>
    )

}