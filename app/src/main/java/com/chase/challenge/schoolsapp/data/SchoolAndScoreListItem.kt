package com.chase.challenge.schoolsapp.data

import androidx.annotation.Keep

@Keep
data class SchoolAndScoreListItem(
    val school_name: String,
    val phone_number: String,
    val overview_paragraph: String,
    val dbn: String,
    val website: String,
    var num_of_sat_test_takers: String,
    var sat_critical_reading_avg_score: String,
    var sat_math_avg_score: String,
    var sat_writing_avg_score: String,
)