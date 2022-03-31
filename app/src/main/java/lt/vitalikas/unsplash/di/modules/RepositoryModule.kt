package lt.vitalikas.unsplash.di.modules

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import lt.vitalikas.unsplash.data.repositories.UserAuthServiceImpl
import lt.vitalikas.unsplash.data.repositories.PhotosRepositoryImpl
import lt.vitalikas.unsplash.data.repositories.OnboardingRepositoryImpl
import lt.vitalikas.unsplash.data.repositories.ProfileRepositoryImpl
import lt.vitalikas.unsplash.domain.repositories.UserAuthService
import lt.vitalikas.unsplash.domain.repositories.PhotosRepository
import lt.vitalikas.unsplash.domain.repositories.OnboardingRepository
import lt.vitalikas.unsplash.domain.repositories.ProfileRepository

@Module
@InstallIn(SingletonComponent::class)
class RepositoryModule {

//    @Binds
//    abstract fun provideAuthRepository(impl: AuthRepositoryImpl): AuthRepository
    @Provides
    fun provideAuthRepository(impl: UserAuthServiceImpl): UserAuthService = impl

//    @Binds
//    abstract fun provideFeedPhotoRepository(impl: FeedPhotosRepositoryImpl): FeedPhotosRepository
    @Provides
    fun provideFeedPhotoRepository(impl: PhotosRepositoryImpl): PhotosRepository = impl

//    @Binds
//    abstract fun provideOnboardingRepository(impl: OnboardingRepositoryImpl): OnboardingRepository
    @Provides
    fun provideOnboardingRepository(impl: OnboardingRepositoryImpl): OnboardingRepository = impl

//    @Binds
//    abstract fun provideProfileRepository(impl: ProfileRepositoryImpl): ProfileRepository
    @Provides
    fun provideProfileRepository(impl: ProfileRepositoryImpl): ProfileRepository = impl
}