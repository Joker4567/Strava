package com.skillbox.shared_model

data class Athlete(
        val id: Long,
        val username: String?,
        val resource_state: Int,
        val firstname: String,
        val lastname: String,
        val city: String,
        val sex: Sex,
        val profile_medium: String,
        val profile: String
)

enum class Sex {
    M, F
}
