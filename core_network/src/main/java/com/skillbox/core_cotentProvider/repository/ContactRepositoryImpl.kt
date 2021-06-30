package com.skillbox.core_cotentProvider.repository

import android.content.Context
import android.database.Cursor
import android.provider.ContactsContract
import com.skillbox.shared_model.contact.Contact
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ContactRepositoryImpl @Inject constructor() : ContactRepository {

    private val avatars = arrayListOf(
            "https://www.w3schools.com/howto/img_avatar.png",
            "https://www.w3schools.com/w3css/img_avatar5.png",
            "https://image.freepik.com/free-vector/man-avatar-profile-on-round-icon_24640-14046.jpg",
            "https://www.w3schools.com/w3images/avatar6.png"
    )

    override suspend fun getAllContacts(context: Context): List<Contact> = withContext(Dispatchers.IO) {
        context.contentResolver.query(
                ContactsContract.Contacts.CONTENT_URI,
                null,
                null,
                null,
                null
        )?.use {
            getContactsFromCursor(it, context)
        }.orEmpty()
    }

    private fun getContactsFromCursor(cursor: Cursor, context: Context): List<Contact> {
        if (cursor.moveToFirst().not()) return emptyList()
        val list: MutableList<Contact> = mutableListOf()
        do {
            val nameIndex = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME_PRIMARY)
            val name = cursor.getString(nameIndex).orEmpty()

            val idIndex = cursor.getColumnIndex(ContactsContract.Contacts._ID)
            val id = cursor.getLong(idIndex)

            list.add(
                    Contact(
                            id = id,
                            name = name,
                            numbers = getPhonesForContact(id, context),
                            avatar = avatars.random()
                    )
            )
        } while (cursor.moveToNext())
        return list
    }

    private fun getPhonesForContact(id: Long, context: Context): List<String> {
        return context.contentResolver.query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                null,
                "${ContactsContract.CommonDataKinds.Phone.CONTACT_ID} = ?",
                arrayOf(id.toString()),
                null
        )?.use {
            getPhonesFromCursor(it)
        }.orEmpty()
    }

    private fun getPhonesFromCursor(cursor: Cursor): List<String> {
        if (cursor.moveToFirst().not()) return emptyList()
        val list: MutableList<String> = mutableListOf()
        do {
            val numberIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)
            val number = cursor.getString(numberIndex)
            list.add(number)
        } while (cursor.moveToNext())
        return list
    }
}