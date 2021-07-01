package com.skillbox.core.platform

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.skillbox.core.state.StateCache
import com.skillbox.core_network.utils.Failure
import com.skillbox.shared_model.ToastModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

abstract class BaseViewModel : ViewModel() {

    val mainState = MutableLiveData<Failure>()
    val localState = MutableLiveData<ToastModel>()

    protected fun handleState(state: Failure) {
        mainState.value = state
        if(state != Failure.CacheError) {
            localState.postValue(ToastModel("Error: Ascent could not load feed.", isLocal = false, isError = true))
        } else {
            StateCache.changeToolbarTitle(true)
        }
    }

    protected fun handleLocal(isLocal: Boolean) {
        if(isLocal) {
            localState.postValue(ToastModel("Loaded feed from cache", isLocal = isLocal))
        } else {
            localState.postValue(ToastModel("", isLocal = false))
        }
    }

    protected fun launch(func: suspend () -> Unit) =
        viewModelScope.launch(Dispatchers.Main) { func.invoke() }

    protected fun launchIO(func: suspend () -> Unit) =
        viewModelScope.launch(Dispatchers.IO) { func.invoke() }
}
