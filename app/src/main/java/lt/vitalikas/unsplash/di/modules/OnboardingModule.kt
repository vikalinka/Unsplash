package lt.vitalikas.unsplash.di.modules

import android.content.Context
import android.content.SharedPreferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import lt.vitalikas.unsplash.R
import lt.vitalikas.unsplash.data.services.onboarding_service.OnboardingService

@Module
@InstallIn(SingletonComponent::class)
class OnboardingModule {

    @Provides
    fun provideSharedPrefs(context: Context): SharedPreferences =
        context.getSharedPreferences(
            context.getString(R.string.onboarding_prefs),
            Context.MODE_PRIVATE
        )

    @Provides
    fun provideOnboardingService(): OnboardingService = OnboardingService()
}