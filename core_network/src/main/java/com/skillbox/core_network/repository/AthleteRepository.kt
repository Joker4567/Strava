package com.skillbox.core_network.repository

import com.skillbox.core_network.utils.State
import com.skillbox.shared_model.Athlete

interface AthleteRepository {
    suspend fun getAthlete(
            onSuccess: (Athlete?) -> Unit,
            onState: (State) -> Unit
    )

    suspend fun getListAthlete(
            onSuccess: (List<Athlete>) -> Unit,
            onState: (State) -> Unit
    )
}