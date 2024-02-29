package com.chase.challenge.schoolsapp.viewmodels

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.chase.challenge.schoolsapp.network.NYCSchoolsRestClient
import com.chase.challenge.schoolsapp.adapter.SchoolsListItemAdapter
import com.chase.challenge.schoolsapp.data.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception

class NYCSchoolsViewModel (private val nycRestClient: NYCSchoolsRestClient) : ViewModel() {
    private val _schoolListLiveData = MutableLiveData<SchoolList>()
    val schoolListLiveData: LiveData<SchoolList> get() = _schoolListLiveData

    private val _schoolScoreLiveData = MutableLiveData<SchoolScoreList>()
    val schoolScoreLiveData: LiveData<SchoolScoreList> get() = _schoolScoreLiveData

    private var dataAdapter: SchoolsListItemAdapter = SchoolsListItemAdapter()

    private var schoolList  = SchoolList()

    fun getAdapter(): SchoolsListItemAdapter {
        return dataAdapter
    }

    fun setSchoolsData(data: SchoolList){
        this.schoolList = data
    }

    fun setAdapterData(data: SchoolAndScoreList){
        dataAdapter.setData(data)
        dataAdapter.notifyDataSetChanged()
    }

    fun fetchListOfSchools() {
        CoroutineScope(Dispatchers.Main).launch {
            try {
                val listOfSchools = nycRestClient.getListOfSchools()
                withContext(Dispatchers.Main) {
                    _schoolListLiveData.value = listOfSchools
                }
            }catch (e: Exception){
                throw SocketTimeoutException("API call timed out")
            }
        }
    }

    fun fetchSchoolScores() {
        CoroutineScope(Dispatchers.Main).launch {
            try {
                val scoreOfSchools = nycRestClient.getScoreOfSchools()
                withContext(Dispatchers.Main) {
                    _schoolScoreLiveData.value = scoreOfSchools
                }
            }catch (e: Exception){
                throw SocketTimeoutException("API call timed out")
            }
        }
    }

    fun mergeSchoolAndScores(schoolScoreList: SchoolScoreList): SchoolAndScoreList {
        val results = SchoolAndScoreList()
        val schoolsById: Map<String, SchoolListItem> = schoolList.associateBy { it.dbn }
        for (item in schoolScoreList) {
            if(schoolsById[item.dbn] != null){
                schoolsById[item.dbn]?.let { createSchoolAndScoreListItem(it, item) }?.let { results.add(it) }
            }
        }
        return results
    }

    private fun createSchoolAndScoreListItem(nycSchool: SchoolListItem, satScore: SchoolScoreListItem?): SchoolAndScoreListItem = SchoolAndScoreListItem(
        dbn = nycSchool.dbn.orEmpty(),
        school_name = nycSchool.school_name.orEmpty(),
        overview_paragraph = nycSchool.overview_paragraph.orEmpty(),
        phone_number = nycSchool.phone_number.orEmpty(),
        website = nycSchool.website.orEmpty(),
        num_of_sat_test_takers = satScore?.num_of_sat_test_takers.orEmpty(),
        sat_critical_reading_avg_score = satScore?.sat_critical_reading_avg_score.orEmpty(),
        sat_math_avg_score = satScore?.sat_math_avg_score.orEmpty(),
        sat_writing_avg_score = satScore?.sat_writing_avg_score.orEmpty()
    )

    fun isConnectedToNetwork(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (connectivityManager != null) {
            val capabilities =
                connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
            if (capabilities != null) {
                if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)
                    || capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
                    || capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                    return true
                }
            }
        }
        return false
    }
}

class SocketTimeoutException(message: String) : Exception(message)