package mick.studio.itsfuntorun.ui.run

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseUser
import mick.studio.itsfuntorun.firebase.FirebaseDBManager
import mick.studio.itsfuntorun.models.RunManager
import mick.studio.itsfuntorun.models.RunModel
import timber.log.Timber

class RunViewModel : ViewModel() {

    private val status = MutableLiveData<Boolean>()

    val observableStatus: LiveData<Boolean>
        get() = status

    fun addRun(
        firebaseUser: MutableLiveData<FirebaseUser>,
        run: RunModel
    ) {
        status.value = try {
            FirebaseDBManager.create(firebaseUser, run)
            Timber.d("completed ${run}")

            true
        } catch (e: IllegalArgumentException) {
            false
        }
    }

}
