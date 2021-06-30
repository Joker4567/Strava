package com.skillbox.shared_model.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.skillbox.shared_model.network.Sex
import java.io.Serializable

@Entity(
        indices = [Index(value = arrayOf("firstname", "lastname"), unique = true)]
)
data class AthleteEntities(
        @PrimaryKey(autoGenerate = true)
        val id: Long,
        @ColumnInfo(name = "username")
        val username: String?,
        @ColumnInfo(name = "resource_state")
        val resource_state: Int,
        @ColumnInfo(name = "firstname")
        val firstname: String,
        @ColumnInfo(name = "lastname")
        val lastname: String,
        @ColumnInfo(name = "city")
        val city: String?,
        @ColumnInfo(name = "sex")
        val sex: Sex,
        @ColumnInfo(name = "profile_medium")
        val profile_medium: String?,
        @ColumnInfo(name = "profile")
        val profile: String?,
        @ColumnInfo(name = "friend")
        val friend: Int?,
        @ColumnInfo(name = "follower")
        val follower: Int?,
        @ColumnInfo(name = "country")
        val country: String?,
        @ColumnInfo(name = "weight")
        val weight: Int? = 0
) : Serializable
