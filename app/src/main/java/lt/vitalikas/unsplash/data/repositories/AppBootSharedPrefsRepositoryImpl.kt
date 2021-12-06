package lt.vitalikas.unsplash.data.repositories

import android.content.Context
import android.content.SharedPreferences
import dagger.hilt.android.qualifiers.ApplicationContext
import lt.vitalikas.unsplash.R
import lt.vitalikas.unsplash.domain.repositories.AppBootSharedPrefsRepository
import javax.inject.Inject

class AppBootSharedPrefsRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context
) :
    AppBootSharedPrefsRepository {

    override fun createSharedPrefs(): SharedPreferences =
        context.getSharedPreferences(
            context.getString(R.string.onboarding_prefs),
            Context.MODE_PRIVATE
        )

    override fun createValue(sharedPrefs: SharedPreferences, key: String, value: Boolean): Boolean =
        sharedPrefs.getBoolean(key, value)

    override fun updateValue(sharedPrefs: SharedPreferences, key: String, value: Boolean) =
        sharedPrefs.edit().putBoolean(key, value).apply()
}