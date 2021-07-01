package com.skillbox.strava.ui.fragment.contact

import android.content.Context
import com.skillbox.core.platform.BaseViewModel
import com.skillbox.core.utils.SingleLiveEvent
import com.skillbox.core_cotentProvider.repository.ContactRepository
import com.skillbox.shared_model.contact.BaseContact
import com.skillbox.strava.R
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ContactViewModel @Inject constructor(
        private val repository: ContactRepository
) : BaseViewModel() {

    val contactObserver = SingleLiveEvent<List<BaseContact>>()
    val loadDataObserver = SingleLiveEvent<Boolean>()

    fun getContacts(context: Context) {
        loadDataObserver.postValue(true)
        launchIO {
            try {
                contactObserver.postValue(repository.getAllContacts(context, R.drawable.ic_error_contact))
            } catch (e: Exception) {
                contactObserver.postValue(arrayListOf())
            } finally {
                loadDataObserver.postValue(false)
            }
        }
    }
}