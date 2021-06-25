package com.skillbox.core.platform

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.skillbox.core.utils.Event
import com.skillbox.core_network.utils.State
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

abstract class BaseViewModel : ViewModel() {

    val mainState = MutableLiveData<Event<State>>()

    protected fun handleState(state: State) {
        if (state is State.Error) {
            mainState.value = Event(state)
        } else
            mainState.value = Event(state)
    }

    protected fun launch(func: suspend () -> Unit) =
        viewModelScope.launch(Dispatchers.Main) { func.invoke() }

    protected fun launchIO(func: suspend () -> Unit) =
        viewModelScope.launch(Dispatchers.IO) { func.invoke() }
}
