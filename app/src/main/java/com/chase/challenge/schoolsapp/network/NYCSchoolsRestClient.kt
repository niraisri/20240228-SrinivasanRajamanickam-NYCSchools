package com.chase.challenge.schoolsapp.network

import com.chase.challenge.schoolsapp.data.SchoolList
import com.chase.challenge.schoolsapp.data.SchoolScoreList
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class NYCSchoolsRestClient  {
    private val httpLoggingInterceptor = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
    private val httpClient = OkHttpClient.Builder().addInterceptor(httpLoggingInterceptor)


    private val retrofit = Retrofit.Builder()
        .baseUrl("https://data.cityofnewyork.us/")
        .addConverterFactory(GsonConverterFactory.create())
        .client(httpClient.build())
        .build()

    var cityOfNewYorkApi: NYCSchoolsApi? = retrofit.create(NYCSchoolsApi::class.java)

    suspend fun getListOfSchools(): SchoolList? {
        return cityOfNewYorkApi?.getListOfSchools()
    }

    suspend fun getScoreOfSchools(): SchoolScoreList? {
        return cityOfNewYorkApi?.getScoreOfSchools()
    }
}