package mick.studio.itsfuntorun.ui.run

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import mick.studio.itsfuntorun.models.RunManager
import mick.studio.itsfuntorun.models.RunModel

class RunViewModel : ViewModel() {

    private val status = MutableLiveData<Boolean>()

    val observableStatus: LiveData<Boolean>
        get() = status

    fun addDonation(donation: RunModel) {
        status.value = try {
            RunManager.create(donation)
            true
        } catch (e: IllegalArgumentException) {
            false
        }
    }
}
