package lt.vitalikas.unsplash.data.prefsstore

import androidx.datastore.preferences.core.Preferences
import kotlinx.coroutines.flow.Flow

interface PrefsStore {

    fun isOnboardingsFinished(): Flow<Boolean>
    suspend fun finishOnboardings(): Preferences
}
