package com.skillbox.core.platform

import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import com.skillbox.core.extensions.observeEvent
import com.skillbox.core.state.StateCache
import com.skillbox.core_network.utils.Failure
import com.skillbox.core_network.utils.State
import com.skillbox.shared_model.ToastModel

abstract class BaseActivity() : AppCompatActivity() {

    private var layout: Int? = null

    constructor(@LayoutRes layoutResId: Int) : this() {
        layout = layoutResId
    }

    open val screenViewModel: BaseViewModel?
        get() = null

    abstract fun initInterface(savedInstanceState: Bundle?)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        layout?.let {
            setContentView(it)
        }
        initInterface(savedInstanceState)
        observeBaseLiveData()
    }

    open fun observeBaseLiveData() {
        screenViewModel?.let { vm ->
            vm.mainState.observe(this, ::handleState)
            vm.localState.observe(this, ::localData)
        }
    }

    open fun handleState(state: Failure) {

    }

    open fun localData(localToast: ToastModel) {

    }

    override fun onDestroy() {
        screenViewModel?.let { vm ->
            vm.mainState.removeObserver {  }
            vm.localState.removeObserver {  }
        }
        super.onDestroy()
    }
}
