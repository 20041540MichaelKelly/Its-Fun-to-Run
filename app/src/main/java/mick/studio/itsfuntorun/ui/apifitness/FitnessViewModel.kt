package mick.studio.itsfuntorun.ui.apifitness

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import mick.studio.itsfuntorun.models.fitness.FitnessManager
import mick.studio.itsfuntorun.models.fitness.FitnessModel
import timber.log.Timber

class FitnessViewModel : ViewModel() {
    private val myApiFitnessList = MutableLiveData<List<FitnessModel>>()

    val observableApiFoodItemsList: LiveData<List<FitnessModel>>
        get() = myApiFitnessList

    init {
        load()
    }

    fun load() {
        try {
            FitnessManager.findAll(myApiFitnessList)
            Timber.i("Retrofit Success : $myApiFitnessList.value")
        }
        catch (e: Exception) {
            Timber.i("Retrofit Error : $e.message")
        }
    }

    fun filterApi(muscle: String) {
        try {
            FitnessManager.findAllByFilter(muscle, myApiFitnessList)
            Timber.i("Retrofit Success : $myApiFitnessList.value")
        }
        catch (e: Exception) {
            Timber.i("Retrofit Error : $e.message")
        }
    }

}