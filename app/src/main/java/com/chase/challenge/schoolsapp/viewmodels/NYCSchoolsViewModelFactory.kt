package com.chase.challenge.schoolsapp.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.chase.challenge.schoolsapp.network.NYCSchoolsRestClient
import kotlin.jvm.Throws

class NYCSchoolsViewModelFactory : ViewModelProvider.Factory {

    @Throws(IllegalArgumentException::class)
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return NYCSchoolsViewModel(nycRestClient = NYCSchoolsRestClient()) as T

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}