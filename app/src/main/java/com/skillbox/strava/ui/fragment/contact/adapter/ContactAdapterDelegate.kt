package com.skillbox.strava.ui.fragment.contact.adapter

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.hannesdorfmann.adapterdelegates4.AbsListItemAdapterDelegate
import com.skillbox.core.extensions.inflate
import com.skillbox.shared_model.contact.Contact
import com.skillbox.strava.R
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_contact.*

class ContactAdapterDelegate (
        private val onItemClick: (Contact: Contact) -> Unit
) :
        AbsListItemAdapterDelegate<Contact, Contact, ContactAdapterDelegate.Holder>() {

    override fun onCreateViewHolder(parent: ViewGroup): Holder {
        return Holder(
                parent.inflate(R.layout.item_contact),
                onItemClick
        )
    }

    override fun isForViewType(
            item: Contact,
            items: MutableList<Contact>,
            position: Int
    ): Boolean {
        return true
    }

    override fun onBindViewHolder(item: Contact, holder: Holder, payloads: MutableList<Any>) {
        holder.bind(item)
    }

    class Holder(
            override val containerView: View,
            private val onItemClick: (contact: Contact) -> Unit
    ) : RecyclerView.ViewHolder(containerView),
            LayoutContainer {

        fun bind(item: Contact) {
            containerView.setOnClickListener {
                onItemClick(item)
            }
            item_textViewName.text = item.name
            if(item.numbers.isEmpty()) item_textViewNumber.text = ""
            else item_textViewNumber.text = item.numbers.first() // Текст уже должен приходить готовым в модельке
            Glide.with(itemView)
                    .load(item.avatar ?: R.drawable.ic_error_contact)
                    .placeholder(R.drawable.ic_placeholder_contact)
                    .error(R.drawable.ic_error_contact)
                    .transform(CircleCrop())
                    .into(item_image)
        }
    }
}