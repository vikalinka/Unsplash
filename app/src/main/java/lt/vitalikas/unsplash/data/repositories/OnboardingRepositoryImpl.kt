package lt.vitalikas.unsplash.data.repositories

import android.content.SharedPreferences
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import lt.vitalikas.unsplash.R
import lt.vitalikas.unsplash.domain.models.OnboardingItem
import lt.vitalikas.unsplash.domain.repositories.OnboardingRepository
import lt.vitalikas.unsplash.ui.onboarding_screen.OnboardingStatus
import javax.inject.Inject

class OnboardingRepositoryImpl @Inject constructor(
    private val sharedPrefs: SharedPreferences,
    private val ioDispatcher: CoroutineDispatcher
) : OnboardingRepository {

    override suspend fun getOnboardingStatus(key: String, defaultValue: Boolean): OnboardingStatus =
        withContext(ioDispatcher) {
            return@withContext when (sharedPrefs.getBoolean(key, defaultValue)) {
                true -> OnboardingStatus.Finished
                false -> OnboardingStatus.NotFinished
            }
        }

    override suspend fun updateOnboardingStatus(key: String, value: Boolean) =
        withContext(ioDispatcher) {
            sharedPrefs
                .edit()
                .putBoolean(key, value)
                .apply()
        }

    override fun createOnboardingItems(): List<OnboardingItem> =
        listOf(
            OnboardingItem(
                R.drawable.onboarding1,
                R.string.onboarding_title_1,
                R.string.onboarding_text_1
            ),
            OnboardingItem(
                R.drawable.onboarding2,
                R.string.onboarding_title_2,
                R.string.onboarding_text_2
            ),
            OnboardingItem(
                R.drawable.onboarding3,
                R.string.onboarding_title_3,
                R.string.onboarding_text_3
            )
        )
}