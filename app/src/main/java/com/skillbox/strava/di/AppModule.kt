package com.skillbox.strava.di

import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import com.skillbox.core_network.utils.ErrorHandler

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Singleton
    @Provides
    fun provideErrorHandler(): ErrorHandler {
        return ErrorHandler()
    }
}