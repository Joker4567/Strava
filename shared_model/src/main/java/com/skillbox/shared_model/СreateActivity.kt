package com.skillbox.shared_model

data class СreateActivity(
        val id: Long,
        val name: String,
        val type: String,
        val start_date: String,
        val elapsed_time: Int,
        val description: String = "",
        val distance: Float,
        val total_elevation_gain: Int = 0
)
