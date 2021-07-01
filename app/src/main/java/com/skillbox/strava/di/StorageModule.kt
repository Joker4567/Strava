package com.skillbox.strava.di

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import com.skillbox.core_db.pref.Pref
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
object StorageModule {

    @RequiresApi(Build.VERSION_CODES.M)
    @Provides
    fun provideSharedPreferences(@ApplicationContext context: Context): Pref {
        return Pref(context)
    }
}