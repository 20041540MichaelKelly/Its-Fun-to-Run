package mick.studio.itsfuntorun.firebase

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import mick.studio.itsfuntorun.models.users.UserModel
import mick.studio.itsfuntorun.models.users.UserStore
import timber.log.Timber

class FirebaseAuthManager(application: Application) {
    private var application: Application? = null

    var firebaseAuth: FirebaseAuth? = null
    var liveFirebaseUser = MutableLiveData<FirebaseUser>()
    var loggedOut = MutableLiveData<Boolean>()
    var errorStatus = MutableLiveData<Boolean>()

    init {
        this.application = application
        firebaseAuth = FirebaseAuth.getInstance()

        if (firebaseAuth!!.currentUser != null) {
            liveFirebaseUser.postValue(firebaseAuth!!.currentUser)
            loggedOut.postValue(false)
            errorStatus.postValue(false)
        }
    }

    fun login(email: String?, password: String?) {
        firebaseAuth!!.signInWithEmailAndPassword(email!!, password!!)
            .addOnCompleteListener(application!!.mainExecutor, { task ->
                if (task.isSuccessful) {
                    liveFirebaseUser.postValue(firebaseAuth!!.currentUser)
                    errorStatus.postValue(false)
                } else {
                    Timber.i( "Login Failure: $task.exception!!.message")
                    errorStatus.postValue(true)
                }
            })
    }

    fun register( user:UserModel) {
        firebaseAuth!!.createUserWithEmailAndPassword(user.email.trim(), user.password)
            .addOnCompleteListener(application!!.mainExecutor, { task ->
                if (task.isSuccessful) {
                    liveFirebaseUser.postValue(firebaseAuth!!.currentUser)
                    errorStatus.postValue(false)
                    user.uid = firebaseAuth!!.currentUser?.uid
                    FirebaseDBManager.createUser(user)
                } else {
                    Timber.i( "Registration Failure: $task.exception!!.message")
                    errorStatus.postValue(true)
                }
            })
    }


    fun logOut() {
        firebaseAuth!!.signOut()
        loggedOut.postValue(true)
        errorStatus.postValue(false)
    }
}