package lt.vitalikas.unsplash.di.modules

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import lt.vitalikas.unsplash.data.repositories.OnboardingRepositoryImpl
import lt.vitalikas.unsplash.domain.repositories.OnboardingRepository

@Module
@InstallIn(ViewModelComponent::class)
abstract class OnboardingRepositoryModule {

    @Binds
    abstract fun provideRepository(impl: OnboardingRepositoryImpl): OnboardingRepository
}