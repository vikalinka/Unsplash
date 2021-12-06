package lt.vitalikas.unsplash.domain.repositories

import android.content.SharedPreferences

interface AppBootSharedPrefsRepository {

    fun createSharedPrefs(): SharedPreferences
    fun createValue(sharedPrefs: SharedPreferences, key: String, value: Boolean): Boolean
    fun updateValue(sharedPrefs: SharedPreferences, key: String, value: Boolean)
}