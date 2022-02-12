package lt.vitalikas.unsplash.di.modules

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.components.SingletonComponent
import lt.vitalikas.unsplash.data.use_cases.DownloadPhotoUseCaseImpl
import lt.vitalikas.unsplash.data.use_cases.GetFeedPhotoDetailsUseCaseImpl
import lt.vitalikas.unsplash.data.use_cases.GetFeedPhotosUseCaseImpl
import lt.vitalikas.unsplash.data.use_cases.GetProfileDataUseCaseImpl
import lt.vitalikas.unsplash.domain.use_cases.DownloadPhotoUseCase
import lt.vitalikas.unsplash.domain.use_cases.GetFeedPhotoDetailsUseCase
import lt.vitalikas.unsplash.domain.use_cases.GetFeedPhotosUseCase
import lt.vitalikas.unsplash.domain.use_cases.GetProfileDataUseCase

@Module
@InstallIn(SingletonComponent::class)
class UseCaseModule {

//    @Binds
//    abstract fun provideGetProfileDataUseCase(impl: GetProfileDataUseCaseImpl): GetProfileDataUseCase
    @Provides
    fun provideGetProfileDataUseCase(impl: GetProfileDataUseCaseImpl): GetProfileDataUseCase = impl

//    @Binds
//    abstract fun provideGetFeedPhotosUseCase(impl: GetFeedPhotosUseCaseImpl): GetFeedPhotosUseCase
    @Provides
    fun provideGetFeedPhotosUseCase(impl: GetFeedPhotosUseCaseImpl): GetFeedPhotosUseCase = impl

//    @Binds
//    abstract fun provideGetFeedPhotoDetailsUseCase(impl: GetFeedPhotoDetailsUseCaseImpl): GetFeedPhotoDetailsUseCase
    @Provides
    fun provideGetFeedPhotoDetailsUseCase(impl: GetFeedPhotoDetailsUseCaseImpl): GetFeedPhotoDetailsUseCase = impl

//    @Binds
//    abstract fun provideDownloadPhotoUseCase(impl: DownloadPhotoUseCaseImpl): DownloadPhotoUseCase
    @Provides
    fun provideDownloadPhotoUseCase(impl: DownloadPhotoUseCaseImpl): DownloadPhotoUseCase = impl
}