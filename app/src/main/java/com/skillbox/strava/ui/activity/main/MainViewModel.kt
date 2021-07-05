package com.skillbox.strava.ui.activity.main

import android.content.Context
import android.os.Build
import android.util.Log
import com.skillbox.core.extensions.getDate
import com.skillbox.core.platform.BaseViewModel
import com.skillbox.core.utils.SingleLiveEvent
import com.skillbox.core_db.pref.Pref
import com.skillbox.core_network.repository.AthleteRepository
import com.skillbox.core_network.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.*
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
        private val repository: AuthRepository,
        @ApplicationContext private val appContext: Context, // У тебя здесь даже предупреждение что так делать не надо :)
        private val repositoryAthlete: AthleteRepository
) : BaseViewModel() {

    val reAuthStateObserver = SingleLiveEvent<Boolean>()
    val showNotificationObserver = SingleLiveEvent<Boolean>()

    init {
        checkRunner()
    }

    fun exit() {
        val token = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            Pref(appContext).accessToken // Советую тебе инжектировать сразу префы, а не контекст
        else
            ""

        launchIO {
            repository.reauthorize(token, ::handleState)?.let { _ ->
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    Pref(appContext).clearProfile()
                }
                reAuthStateObserver.postValue(true)
            }
        }
    }

    private fun checkRunner() {
        launchIO {
            repositoryAthlete.getLastAthleteDate()?.let { athlete ->
                val dateLocalDB = getDate(athlete.start_date)
                val dateNow = Date(System.currentTimeMillis())
                if (dateLocalDB < dateNow) {
                    val dayPref = Pref(appContext).checkDay // Здесь нет проверки на версию
                    if (dayPref != dateNow.day) {
                        Pref(appContext).checkDay = dateNow.day
                        //Отправляем уведомление об напоминании
                        showNotificationObserver.postValue(true)
                    }
                }
            }
        }
    }

}