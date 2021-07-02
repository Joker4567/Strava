package com.skillbox.core.platform

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import com.skillbox.core.extensions.observeEvent
import com.skillbox.core.state.StateCache
import com.skillbox.core.utils.Event
import com.skillbox.core_network.utils.Failure
import com.skillbox.core_network.utils.State
import com.skillbox.shared_model.ToastModel

abstract class BaseFragment() : Fragment() {

    open val screenViewModel: BaseViewModel? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        screenViewModel?.let { vm ->
            vm.mainState.observe(viewLifecycleOwner, ::handleState)
            vm.localState.observe(viewLifecycleOwner, ::localData)
        }
    }

    open fun onBackPressed(): Boolean = false

    open fun handleState(state: Event<State>) {

    }

    open fun localData(localToast:ToastModel) {

    }

    override fun onStop() {
        super.onStop()
        hideSoftKeyboard()
    }

    override fun onDestroyView() {
        screenViewModel?.let { vm ->
            vm.mainState.removeObserver {  }
            vm.localState.removeObserver {  }
        }
        super.onDestroyView()
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