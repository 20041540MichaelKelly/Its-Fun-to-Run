package mick.studio.itsfuntorun.api

import mick.studio.itsfuntorun.models.fitness.FitnessModel
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface FitnessService {
    @GET("exercises?muscle=hamstrings")
    fun getall(): Call<List<FitnessModel>>

    @GET("exercises?muscle=")
    fun getallbydescription(@Query(value = "muscle") muscle: String?): Call<List<FitnessModel>>?


}