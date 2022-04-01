package lt.vitalikas.unsplash.data.repositories

import android.content.SharedPreferences
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import lt.vitalikas.unsplash.data.services.onboarding_service.OnboardingService
import lt.vitalikas.unsplash.domain.models.onboarding.OnboardingItem
import lt.vitalikas.unsplash.domain.repositories.OnboardingRepository
import lt.vitalikas.unsplash.ui.onboarding_screen.OnboardingStatus
import javax.inject.Inject

class OnboardingRepositoryImpl @Inject constructor(
    private val sharedPrefs: SharedPreferences,
    private val ioDispatcher: CoroutineDispatcher,
    private val onboardingService: OnboardingService
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

    override fun getOnboardingItems(): List<OnboardingItem> = onboardingService.onboardings
}