package mick.studio.itsfuntorun.ui.runlist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import mick.studio.itsfuntorun.models.RunManager
import mick.studio.itsfuntorun.models.RunModel

class RunListViewModel : ViewModel() {
    private val runsList = MutableLiveData<List<RunModel>>()

    val observableRunsList: LiveData<List<RunModel>>
        get() = runsList

    init {
        load()
    }

    fun load() {
        runsList.value = RunManager.findAll()
    }
}