package com.example.dm_project.network

import com.squareup.moshi.Moshi
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object API(private val context: Context) {

    companion object {
        private const val BASE_URL = "https://android-tasks-api.herokuapp.com/api/"
        private const val TOKEN = "votre token"
        lateinit var INSTANCE: Api
    }

    private const val BASE_URL = "https://android-tasks-api.herokuapp.com/api/"
    private const val TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJ1c2VyX2lkIjozOCwiZXhwIjoxNjA4MTM0MTg2fQ.2y_qcMjje-9RVv4-TvOUcIDj2P_pL-00Zp0yHLGVJ54"
    private val moshi = Moshi.Builder().build()

    private val okHttpClient by lazy {
        OkHttpClient.Builder().addInterceptor {
                chain ->
                    val newRequest = chain.request().newBuilder()
                    .addHeader("Authorization", "Bearer $TOKEN")
                    .build()
                    chain.proceed(newRequest)
        }.build()
    }

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()

    val userService: UserService by lazy { retrofit.create(UserService::class.java) }
    val tasksService: TasksService by lazy { retrofit.create(TasksService::class.java) }

    fun getToken() : String {
        return PreferenceManager.getDefaultSharedPreferences(context).getString(SHARED_PREF_TOKEN_KEY, "") ?: ""
    }
}