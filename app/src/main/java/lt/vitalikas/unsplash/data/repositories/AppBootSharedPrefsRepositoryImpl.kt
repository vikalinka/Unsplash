package lt.vitalikas.unsplash.data.repositories

import android.content.Context
import android.content.SharedPreferences
import lt.vitalikas.unsplash.R
import lt.vitalikas.unsplash.domain.repositories.AppBootSharedPrefsRepository

class AppBootSharedPrefsRepositoryImpl(private val context: Context) :
    AppBootSharedPrefsRepository {
    override fun get(): SharedPreferences {
        return context.getSharedPreferences(
            context.getString(R.string.onboarding_prefs),
            Context.MODE_PRIVATE
        )
    }

    override fun save(key: String, value: Boolean) {
        get().apply {
            edit()
                .putBoolean(key, value)
                .apply()
        }
    }
}