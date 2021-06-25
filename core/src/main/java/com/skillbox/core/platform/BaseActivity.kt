package com.skillbox.core.platform

import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import com.skillbox.core.extensions.observeEvent
import com.skillbox.core_network.utils.State

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
            observeEvent(vm.mainState, ::handleState)
        }
    }

    open fun handleState(state: State) {
        when (state) {
            is State.Error -> {

            }
        }
    }
}
