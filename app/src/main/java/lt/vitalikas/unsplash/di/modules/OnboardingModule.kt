package lt.vitalikas.unsplash.di.modules

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import lt.vitalikas.unsplash.data.services.onboarding_service.OnboardingService

@Module
@InstallIn(SingletonComponent::class)
class OnboardingModule {

    @Provides
    fun provideOnboardingService(): OnboardingService = OnboardingService()
}
