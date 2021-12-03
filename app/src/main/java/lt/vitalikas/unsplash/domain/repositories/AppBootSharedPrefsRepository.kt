package lt.vitalikas.unsplash.domain.repositories

import android.content.SharedPreferences

interface AppBootSharedPrefsRepository {
    fun get(): SharedPreferences
    fun save(key: String, value: Boolean)
}