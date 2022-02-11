package lt.vitalikas.unsplash.di.modules

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import lt.vitalikas.unsplash.data.use_cases.DownloadPhotoUseCaseImpl
import lt.vitalikas.unsplash.data.use_cases.GetFeedPhotoDetailsUseCaseImpl
import lt.vitalikas.unsplash.data.use_cases.GetFeedPhotosUseCaseImpl
import lt.vitalikas.unsplash.data.use_cases.GetProfileDataUseCaseImpl
import lt.vitalikas.unsplash.domain.use_cases.DownloadPhotoUseCase
import lt.vitalikas.unsplash.domain.use_cases.GetFeedPhotoDetailsUseCase
import lt.vitalikas.unsplash.domain.use_cases.GetFeedPhotosUseCase
import lt.vitalikas.unsplash.domain.use_cases.GetProfileDataUseCase

@Module
@InstallIn(ViewModelComponent::class)
abstract class UseCaseModule {

    @Binds
    abstract fun provideGetProfileDataUseCase(impl: GetProfileDataUseCaseImpl): GetProfileDataUseCase

    @Binds
    abstract fun provideGetFeedPhotosUseCase(impl: GetFeedPhotosUseCaseImpl): GetFeedPhotosUseCase

    @Binds
    abstract fun provideGetFeedPhotoDetailsUseCase(impl: GetFeedPhotoDetailsUseCaseImpl): GetFeedPhotoDetailsUseCase

    @Binds
    abstract fun provideDownloadPhotoUseCase(impl: DownloadPhotoUseCaseImpl): DownloadPhotoUseCase
}