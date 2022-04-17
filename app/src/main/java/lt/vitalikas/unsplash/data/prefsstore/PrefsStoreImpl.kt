package lt.vitalikas.unsplash.data.prefsstore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject

private const val ONBOARDINGS_STORE_NAME = "onboardings_store"
private val Context.onboardingsDataStore: DataStore<Preferences> by preferencesDataStore(
    name = ONBOARDINGS_STORE_NAME
)

class PrefsStoreImpl @Inject constructor(
    val context: Context
) : PrefsStore {

    override fun isOnboardingsFinished(): Flow<Boolean> =
        context.onboardingsDataStore.data
            .catch { exception ->
                if (exception is IOException) {
                    emit(emptyPreferences())
                } else {
                    throw exception
                }
            }.map { preferences ->
                preferences[PreferencesKeys.ONBOARDINGS_KEY] ?: false
            }

    override suspend fun finishOnboardings() = context.onboardingsDataStore.edit { prefs ->
        prefs[PreferencesKeys.ONBOARDINGS_KEY] = !(prefs[PreferencesKeys.ONBOARDINGS_KEY] ?: false)
    }

    private object PreferencesKeys {
        val ONBOARDINGS_KEY = booleanPreferencesKey(ONBOARDINGS_STORE_NAME)
    }
}
