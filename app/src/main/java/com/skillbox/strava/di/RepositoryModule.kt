package com.skillbox.strava.di

import com.skillbox.core_db.pref.Pref
import com.skillbox.core_db.room.dao.AthleteDao
import com.skillbox.core_network.api.AthleteApi
import com.skillbox.core_network.api.AuthApi
import com.skillbox.core_network.repository.AthleteRepository
import com.skillbox.core_network.repository.AthleteRepositoryImpl
import com.skillbox.core_network.repository.AuthRepository
import com.skillbox.core_network.repository.AuthRepositoryImpl
import com.skillbox.core_network.utils.ErrorHandler
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(ViewModelComponent::class)
object RepositoryModule {

    // Тем более такой модуль есть
    // Все репозитории сюда можно занести
    @Provides
    fun provideAuthRepository(
            errorHandler: ErrorHandler,
            authApi: AuthApi,
            pref: Pref
    ): AuthRepository {
        return AuthRepositoryImpl(
                errorHandler = errorHandler,
                api = authApi,
                pref = pref
        )
    }
}