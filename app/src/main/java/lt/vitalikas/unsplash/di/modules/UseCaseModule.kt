package lt.vitalikas.unsplash.di.modules

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import lt.vitalikas.unsplash.data.use_cases.*
import lt.vitalikas.unsplash.domain.use_cases.*

@Module
@InstallIn(SingletonComponent::class)
class UseCaseModule {

    // Using Worker with Hilt @Binds doesn't work

    @Provides
    fun provideUserAuthUseCase(impl: AuthUserUseCaseImpl): AuthUserUseCase = impl

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

    @Provides
    fun provideLikePhotoUseCase(impl: LikePhotoUseCaseImpl): LikePhotoUseCase = impl

    @Provides
    fun provideDislikePhotoUseCase(impl: DislikePhotoUseCaseImpl): DislikePhotoUseCase = impl

    @Provides
    fun provideSearchFeedPhotosUseCase(impl: SearchPhotosUseCaseImpl): SearchPhotosUseCase = impl

    @Provides
    fun provideGetCollectionsUseCase(impl: GetCollectionsUseCaseImpl): GetCollectionsUseCase = impl

    @Provides
    fun provideGetCollectionUseCase(impl: GetCollectionUseCaseImpl): GetCollectionUseCase = impl

    @Provides
    fun provideGetCollectionPhotosUseCase(impl: GetCollectionPhotosUseCaseImpl): GetCollectionPhotosUseCase = impl
}