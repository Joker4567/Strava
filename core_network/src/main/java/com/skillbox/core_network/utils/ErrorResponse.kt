package com.skillbox.core_network.utils

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class ErrorResponse(
    @SerializedName("error")
    val error: String? = null,
    @SerializedName("message")
    val message: String? = null
) : Serializable

/*
{
    "message": "Authorization Error",
    "errors": [
        {
            "resource": "Athlete",
            "field": "access_token",
            "code": "invalid"
        }
    ]
}
*/