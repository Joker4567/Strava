package com.skillbox.core.platform

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import com.skillbox.core.extensions.observeEvent
import com.skillbox.core_network.utils.State

abstract class BaseFragment() : Fragment() {

    open val screenViewModel: BaseViewModel? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        screenViewModel?.let { vm ->
            observeEvent(vm.mainState, ::handleState)
        }
    }

    open fun onBackPressed(): Boolean = false

    open fun handleState(state: State) {
        when (state) {
            is State.Loading -> { }
            is State.Loaded -> { }
            is State.Error -> { }
        }
    }

    override fun onStop() {
        super.onStop()
        hideSoftKeyboard()
    }

    fun showSoftKeyboard(field: View?) {
        field?.let {
            val inputMethodManager =
                activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
            inputMethodManager?.showSoftInput(it, InputMethodManager.SHOW_IMPLICIT)
        }
    }

    fun hideSoftKeyboard() {
        view?.let {
            val inputMethodManager =
                activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
            inputMethodManager?.hideSoftInputFromWindow(it.windowToken, 0)
        }
    }
}