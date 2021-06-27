package com.skillbox.core.state

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch

object StateTitleToolbar {
    private val stateTitle = MutableSharedFlow<String>()
    val titleToolbar: SharedFlow<String> = stateTitle

    fun changeToolbarTitle(isInstallApp: String) {
        CoroutineScope(Dispatchers.IO).launch {
            stateTitle.emit(isInstallApp)
        }
    }
}