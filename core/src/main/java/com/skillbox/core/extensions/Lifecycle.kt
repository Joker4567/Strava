package com.skillbox.core.extensions

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.skillbox.core.utils.Event
import com.skillbox.core.utils.EventObserver
import com.skillbox.core.utils.SingleLiveEvent

fun <T : Any, L : LiveData<T>> LifecycleOwner.observeLifeCycle(liveData: L, body: (T?) -> Unit) =
    liveData.observe(this, Observer(body))

fun <T : Any?, L : LiveData<T>> Fragment.observeFragment(liveData: L, body: (T?) -> Unit) =
    liveData.observe(viewLifecycleOwner, Observer(body))

fun <T : Any?, L : LiveData<Event<T>>> Fragment.observeEvent(liveData: L, body: (T) -> Unit) =
    liveData.observe(viewLifecycleOwner, EventObserver(body))

fun <T : Any?, L : SingleLiveEvent<Event<T>>> Fragment.observeEvent(liveData: L, body: (Event<T>?) -> Unit) =
    liveData.observe(viewLifecycleOwner, Observer(body))

fun <T : Any?, L : LiveData<Event<T>>> AppCompatActivity.observeEvent(
    liveData: L,
    body: (T) -> Unit
) =
    liveData.observe(this, EventObserver(body))