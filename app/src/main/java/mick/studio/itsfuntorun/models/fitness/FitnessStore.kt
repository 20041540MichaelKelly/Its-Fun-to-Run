package mick.studio.itsfuntorun.models.fitness

import androidx.lifecycle.MutableLiveData

interface FitnessStore {
    fun findAll(myApiFitnessList: MutableLiveData<List<FitnessModel>>)
    fun findAllByFilter(
        muscle: String,
        myApiFitnessList: MutableLiveData<List<FitnessModel>>
    )
}