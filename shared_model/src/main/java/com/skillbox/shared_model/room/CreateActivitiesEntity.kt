package com.skillbox.shared_model.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(
        indices = [Index(value = arrayOf("name"), unique = true)]
)
data class CreateActivitiesEntity(
        @PrimaryKey(autoGenerate = true)
        val id: Long,
        @ColumnInfo(name = "name")
        val name: String,
        @ColumnInfo(name = "type")
        val type: String,
        @ColumnInfo(name = "start_date")
        val start_date: String,
        @ColumnInfo(name = "elapsed_time")
        val elapsed_time: Int,
        @ColumnInfo(name = "description")
        val description: String? = "",
        @ColumnInfo(name = "distance")
        val distance: Float? = 0F,
        @ColumnInfo(name = "total_elevation_gain")
        val total_elevation_gain: Int? = 0
) : Serializable
