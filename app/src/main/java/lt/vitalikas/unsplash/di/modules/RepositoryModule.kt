package lt.vitalikas.unsplash.di.modules

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.components.SingletonComponent
import lt.vitalikas.unsplash.data.repositories.AuthRepositoryImpl
import lt.vitalikas.unsplash.data.repositories.FeedPhotosRepositoryImpl
import lt.vitalikas.unsplash.data.repositories.OnboardingRepositoryImpl
import lt.vitalikas.unsplash.data.repositories.ProfileRepositoryImpl
import lt.vitalikas.unsplash.domain.repositories.AuthRepository
import lt.vitalikas.unsplash.domain.repositories.FeedPhotosRepository
import lt.vitalikas.unsplash.domain.repositories.OnboardingRepository
import lt.vitalikas.unsplash.domain.repositories.ProfileRepository

@Module
@InstallIn(SingletonComponent::class)
class RepositoryModule {

//    @Binds
//    abstract fun provideAuthRepository(impl: AuthRepositoryImpl): AuthRepository
    @Provides
    fun provideAuthRepository(impl: AuthRepositoryImpl): AuthRepository = impl

//    @Binds
//    abstract fun provideFeedPhotoRepository(impl: FeedPhotosRepositoryImpl): FeedPhotosRepository
    @Provides
    fun provideFeedPhotoRepository(impl: FeedPhotosRepositoryImpl): FeedPhotosRepository = impl

//    @Binds
//    abstract fun provideOnboardingRepository(impl: OnboardingRepositoryImpl): OnboardingRepository
    @Provides
    fun provideOnboardingRepository(impl: OnboardingRepositoryImpl): OnboardingRepository = impl

//    @Binds
//    abstract fun provideProfileRepository(impl: ProfileRepositoryImpl): ProfileRepository
    @Provides
    fun provideProfileRepository(impl: ProfileRepositoryImpl): ProfileRepository = impl
}