package com.skillbox.strava.ui.fragment.logOut

import android.content.Context
import android.os.Build
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
        @ApplicationContext private val appContext: Context
) : BaseViewModel() {

    val reAuthStateObserver = SingleLiveEvent<Boolean>()

    fun exit() {
        val token = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            Pref(appContext).accessToken
        else
            ""
        launchIO {
            repositoryAuth.reauthorize(token, { token ->
                if(token.isNotEmpty()){
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        Pref(appContext).clearProfile()
                    }
                }
                reAuthStateObserver.postValue(token.isNotEmpty())
            }, ::handleState)
        }
    }
}