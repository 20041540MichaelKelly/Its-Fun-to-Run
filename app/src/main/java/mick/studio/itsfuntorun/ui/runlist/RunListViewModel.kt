package mick.studio.itsfuntorun.ui.runlist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import mick.studio.itsfuntorun.models.RunManager
import mick.studio.itsfuntorun.models.RunModel

class RunListViewModel : ViewModel() {
    private val donationsList = MutableLiveData<List<RunModel>>()

    val observableDonationsList: LiveData<List<RunModel>>
        get() = donationsList

    init {
        load()
    }

    fun load() {
        donationsList.value = RunManager.findAll()
    }
}