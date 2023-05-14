package mick.studio.itsfuntorun.models

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import mick.studio.itsfuntorun.models.users.UserModel

class SharedViewModel(app: Application) : AndroidViewModel(app) {
    private val _runModel = MutableLiveData<RunModel>()
    private val _userModel = MutableLiveData<UserModel>()

    val observableRunModel: LiveData<RunModel>
        get() = _runModel

    val observableUserModel: LiveData<UserModel>
        get() = _userModel

    fun setRunModel(runModel: RunModel) {
        _runModel.value = runModel
    }

    fun setUserModel(userModel: UserModel) {
        _userModel.value = userModel
    }
}