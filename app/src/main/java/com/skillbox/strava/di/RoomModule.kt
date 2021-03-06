package com.skillbox.strava.di

import android.content.Context
import com.skillbox.core_db.room.SkillboxDatabase
import com.skillbox.core_db.room.dao.AthleteDao
import dagger.*
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object RoomModule {

    @Provides
    fun provideBlogDb(@ApplicationContext context: Context): SkillboxDatabase {
        return SkillboxDatabase.buildDataSource(context)
    }

    @Provides
    fun provideGameCardDao(skillboxDatabase: SkillboxDatabase): AthleteDao {
        return skillboxDatabase.getAthleteDao()
    }
}