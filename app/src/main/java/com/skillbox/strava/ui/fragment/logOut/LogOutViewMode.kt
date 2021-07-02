package com.skillbox.strava.ui.fragment.logOut

import android.content.Context
import android.os.Build
import android.util.Log
import com.skillbox.core.platform.BaseViewModel
import com.skillbox.core.utils.SingleLiveEvent
import com.skillbox.core_db.pref.Pref
import com.skillbox.core_network.repository.AthleteRepository
import com.skillbox.core_network.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

@HiltViewModel
class LogOutViewMode @Inject constructor(
        private val repositoryAuth: AuthRepository,
        private val repository: AthleteRepository,
        @ApplicationContext private val appContext: Context
) : BaseViewModel() {

    val reAuthStateObserver = SingleLiveEvent<Boolean>()

    fun exit() {
        val token = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            Pref(appContext).accessToken
        else
            ""
        launchIO {
            repositoryAuth.reauthorize(token, ::handleState)?.let { token ->
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    Pref(appContext).clearProfile()
                }
                launchIO {
                    repository.clearProfile()
                }
                Log.d("LogOutViewMode", "Токен и данные прифиля были успешно очишены")
                reAuthStateObserver.postValue(true)
            }
        }
    }
}