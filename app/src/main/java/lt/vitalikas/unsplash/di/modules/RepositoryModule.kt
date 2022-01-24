package lt.vitalikas.unsplash.di.modules

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import lt.vitalikas.unsplash.data.repositories.AuthRepositoryImpl
import lt.vitalikas.unsplash.data.repositories.FeedPhotosRepositoryImpl
import lt.vitalikas.unsplash.data.repositories.OnboardingRepositoryImpl
import lt.vitalikas.unsplash.data.repositories.ProfileRepositoryImpl
import lt.vitalikas.unsplash.domain.repositories.AuthRepository
import lt.vitalikas.unsplash.domain.repositories.FeedPhotosRepository
import lt.vitalikas.unsplash.domain.repositories.OnboardingRepository
import lt.vitalikas.unsplash.domain.repositories.ProfileRepository

@Module
@InstallIn(ViewModelComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun provideAuthRepository(impl: AuthRepositoryImpl): AuthRepository

    @Binds
    abstract fun provideFeedPhotoRepository(impl: FeedPhotosRepositoryImpl): FeedPhotosRepository

    @Binds
    abstract fun provideOnboardingRepository(impl: OnboardingRepositoryImpl): OnboardingRepository

//    @Provides
//    fun provideRepository(impl: ProfileRepositoryImpl): ProfileRepository {
//        return impl
//    }

    @Binds
    abstract fun provideProfileRepository(impl: ProfileRepositoryImpl): ProfileRepository
}