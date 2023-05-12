package mick.studio.itsfuntorun.models.fitness

import androidx.lifecycle.MutableLiveData
import mick.studio.itsfuntorun.api.FitnessClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber

object FitnessManager : FitnessStore {
    val items = ArrayList<FitnessModel>()

    override fun findAll(myApiFitnessList: MutableLiveData<List<FitnessModel>>) {
        val call = FitnessClient.getApi().getall()

        call.enqueue(object : Callback<List<FitnessModel>> {
            override fun onResponse(call: Call<List<FitnessModel>>, response: Response<List<FitnessModel>>) {
                myApiFitnessList.value = response.body() as List<FitnessModel>
                Timber.i("Retrofit JSON = ${response.body()}"
                )
            }

            override fun onFailure(call: Call<List<FitnessModel>>, t: Throwable) {
                Timber.i("Retrofit Error : $t.message $call.data")
            }

        })
    }

    override fun findAllByFilter( muscle:String, myApiFitnessList: MutableLiveData<List<FitnessModel>>) {
        val call = FitnessClient.getApi().getallbydescription(muscle)

        call?.enqueue(object : Callback<List<FitnessModel>> {
            override fun onResponse(call: Call<List<FitnessModel>>, response: Response <List<FitnessModel>>) {
                myApiFitnessList.value = response.body() as List<FitnessModel>
                Timber.i("Retrofit JSON = ${response.body()}"
                )
            }

            override fun onFailure(call: Call<List<FitnessModel>>, t: Throwable) {
                Timber.i("Retrofit Error : $t.message $call.data")
            }

        })
    }
}