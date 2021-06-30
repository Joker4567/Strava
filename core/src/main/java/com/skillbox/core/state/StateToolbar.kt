package com.skillbox.core.state

import com.skillbox.shared_model.ToolbarModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch

object StateToolbar {
    private val stateTitle = MutableSharedFlow<ToolbarModel>()
    val modelToolbar: SharedFlow<ToolbarModel> = stateTitle

    fun changeToolbarTitle(modelToolbar: ToolbarModel) {
        CoroutineScope(Dispatchers.IO).launch {
            stateTitle.emit(modelToolbar)
        }
    }
}