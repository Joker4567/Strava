package com.skillbox.strava.ui.fragment.contact.adapter

import androidx.recyclerview.widget.DiffUtil
import com.hannesdorfmann.adapterdelegates4.AsyncListDifferDelegationAdapter
import com.skillbox.shared_model.contact.Contact

class ContactAdapter (onItemClick: (position: Contact) -> Unit ) : AsyncListDifferDelegationAdapter<Contact>(
        UserDiffUtilCallback()
) {

    init {
        delegatesManager.addDelegate(
                ContactAdapterDelegate(
                        onItemClick // Я бы сделал или полем или как минимум предоставил бы дефолтное значение для клика
                            // Потому что если вдруг этот адаптер где-то еще понадобится, то тебе придется просто имплементить дефолтное значение, а это не очень хорошо
                )
        )
    }

    class UserDiffUtilCallback : DiffUtil.ItemCallback<Contact>() {
        override fun areItemsTheSame(oldItem: Contact, newItem: Contact): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Contact, newItem: Contact): Boolean {
            return true
        }
    }
}