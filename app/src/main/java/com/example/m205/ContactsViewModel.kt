package com.example.m205

import android.content.Context
import android.provider.CallLog
import android.provider.ContactsContract
import android.provider.MediaStore
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

data class Contact(
    val id: String,
    val name: String,
    val phone: String
)



class ContactsViewModel : ViewModel() {

    var contacts by mutableStateOf<List<Contact>>(emptyList())
        private set

    fun loadContact(context: Context) {

        viewModelScope.launch(Dispatchers.IO) {

            val result = mutableListOf<Contact>()
            val resolver = context.contentResolver

            val cursor = resolver.query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                arrayOf(
                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID,
                    ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                    ContactsContract.CommonDataKinds.Phone.NUMBER
                ),
                null,
                null,
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC"
            )

            cursor?.use {
                while (it.moveToNext()) {
                    val id = it.getString(0)
                    val name = it.getString(1)
                    val phone = it.getString(2)
                    result.add(Contact(
                        id = it.getString(0),
                        name = it.getString(1),
                        phone = it.getString(2)
                    ))
                }
            }

            contacts = result
        }
    }
}