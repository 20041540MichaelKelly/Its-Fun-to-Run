package mick.studio.itsfuntorun.ui.auth

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseUser
import mick.studio.itsfuntorun.firebase.FirebaseAuthManager
import mick.studio.itsfuntorun.firebase.FirebaseDBManager
import mick.studio.itsfuntorun.models.users.UserModel
import timber.log.Timber

class LoginRegisterViewModel(app: Application) : AndroidViewModel(app) {

    var firebaseAuthManager : FirebaseAuthManager = FirebaseAuthManager(app)
    var liveFirebaseUser : MutableLiveData<FirebaseUser> = firebaseAuthManager.liveFirebaseUser

    fun login(email: String?, password: String?) {
        firebaseAuthManager.login(email, password)
    }

    fun registerUserCreate(user:UserModel) {
        try {
           register(user)
            //  addUserInfo(user)

        }  catch (e: IllegalArgumentException) {
        }
    }

    fun register(user:UserModel): Boolean {
        try {
            firebaseAuthManager.register(user)
            return true
        }  catch (e: IllegalArgumentException) {
            return false
        }
    }

    fun addUserInfo(userModel: UserModel) {
            try{
                FirebaseDBManager.createUser(userModel)
                Timber.d("completed ${userModel}")

                true
            } catch (e: IllegalArgumentException) {
                false
            }
    }
}