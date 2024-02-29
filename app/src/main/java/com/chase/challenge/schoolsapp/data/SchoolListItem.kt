package com.chase.challenge.schoolsapp.data

import androidx.annotation.Keep

@Keep
data class SchoolListItem(
    val school_name: String,
    val phone_number: String,
    val overview_paragraph: String,
    val dbn: String,
    val website: String,
)