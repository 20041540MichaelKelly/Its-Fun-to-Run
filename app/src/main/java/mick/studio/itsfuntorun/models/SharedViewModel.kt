package mick.studio.itsfuntorun.models

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class SharedViewModel(app: Application) : AndroidViewModel(app) {
    private val _runModel = MutableLiveData<RunModel>()

    val observableRunModel: LiveData<RunModel>
        get() = _runModel


    fun setRunModel(runModel: RunModel) {
        _runModel.value = runModel
    }
}