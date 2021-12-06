package lt.vitalikas.unsplash.data.repositories

import android.content.Context
import android.content.SharedPreferences
import dagger.hilt.android.qualifiers.ApplicationContext
import lt.vitalikas.unsplash.R
import lt.vitalikas.unsplash.domain.models.OnboardingItem
import lt.vitalikas.unsplash.domain.repositories.OnboardingRepository
import javax.inject.Inject

class OnboardingRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : OnboardingRepository {

    var sharedPrefs: SharedPreferences =
        context.getSharedPreferences(
            context.getString(R.string.onboarding_prefs),
            Context.MODE_PRIVATE
        )

    override fun getOnboardingSharedPrefsValue(key: String, value: Boolean): Boolean =
        sharedPrefs.getBoolean(key, value)

    override fun updateOnboardingSharedPrefsValue(key: String, value: Boolean) =
        sharedPrefs
            .edit()
            .putBoolean(key, value)
            .apply()

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