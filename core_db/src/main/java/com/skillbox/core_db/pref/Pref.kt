package com.skillbox.core_db.pref

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.content.edit
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys

@RequiresApi(Build.VERSION_CODES.M)
class Pref(context: Context) {

    private var masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)
    private var sharedPreferences = EncryptedSharedPreferences.create(
            FileName,
            masterKeyAlias,
            context,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    var authToken: String?
        get() = sharedPreferences.getString(KeyToken, null)
        set(value) {
            sharedPreferences.edit {
                putString(KeyToken, value)
            }
        }

    companion object {
        const val FileName = "SkillboxPreference"
        const val KeyToken = "token"
    }
}