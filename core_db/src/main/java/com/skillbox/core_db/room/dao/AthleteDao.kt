package com.skillbox.core_db.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.skillbox.shared_model.room.AthleteEntities
import com.skillbox.shared_model.room.CreateActivitiesEntity

@Dao
interface AthleteDao {
    //List activities
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAthleteActivities(list: List<CreateActivitiesEntity>)

    @Query("SELECT * FROM CreateActivitiesEntity")
    suspend fun getAthleteActivities() : List<CreateActivitiesEntity>

    @Query("DELETE FROM CreateActivitiesEntity") // Можно и специфические аннотации использовать, но окей
    suspend fun deleteAthleteActivities()

    @Query("SELECT * FROM CreateActivitiesEntity ORDER BY id DESC LIMIT 1")
    suspend fun getAthleteLastDate() : CreateActivitiesEntity?
    //Athlete
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAthlete(athlete: AthleteEntities)

    @Query("SELECT * FROM AthleteEntities LIMIT 1")
    suspend fun getAthlete() : AthleteEntities?

    @Query("DELETE FROM AthleteEntities")
    suspend fun deleteAthlete()
}