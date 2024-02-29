package com.chase.challenge.schoolsapp.network

import com.chase.challenge.schoolsapp.data.SchoolList
import com.chase.challenge.schoolsapp.data.SchoolScoreList
import retrofit2.http.GET

interface NYCSchoolsApi {

    @GET("resource/s3k6-pzi2.json")
    suspend fun getListOfSchools(): SchoolList

    @GET("resource/f9bf-2cp4.json")
    suspend fun getScoreOfSchools(): SchoolScoreList
}