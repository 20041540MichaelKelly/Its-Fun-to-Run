package mick.studio.itsfuntorun.api

import com.google.gson.GsonBuilder
import mick.studio.itsfuntorun.BuildConfig
import mick.studio.itsfuntorun.BuildConfig.API_FITNESS_KEY
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object FitnessClient {
    val serviceURL = "https://api.api-ninjas.com/v1/"

    fun getApi(): FitnessService {
        val gson = GsonBuilder().create()
        val API_KEY = "${API_FITNESS_KEY}"


        val okHttpClient = OkHttpClient.Builder().apply {
            addInterceptor {
                 it.proceed(it.request().newBuilder().addHeader("X-Api-Key", API_KEY).build())}
//                Interceptor { chain ->
//                    val builder = chain.request().newBuilder()
//                    val originalHttpUrl = chain.request().url()
//                    val url = originalHttpUrl.newBuilder().addQueryParameter("X-Api-Key", API_KEY)
//                        .build() //this is so I wont have to keep using it everytime
//                    builder.url(url)
//                    builder.header(API_KEY,"X-Api-Key")
//                    return@Interceptor chain.proceed(builder.build())
//                }
//            )
        }.connectTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build()

        val apiInterface = Retrofit.Builder()
            .baseUrl(serviceURL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(okHttpClient)
            .build()
        return apiInterface.create(FitnessService::class.java)
    }
}
