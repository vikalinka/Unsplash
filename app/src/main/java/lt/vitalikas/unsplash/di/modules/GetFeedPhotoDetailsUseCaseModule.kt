package lt.vitalikas.unsplash.di.modules

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import lt.vitalikas.unsplash.data.use_cases.GetFeedPhotoDetailsUseCaseImpl
import lt.vitalikas.unsplash.domain.use_cases.GetFeedPhotoDetailsUseCase

@Module
@InstallIn(ViewModelComponent::class)
abstract class GetFeedPhotoDetailsUseCaseModule {

    @Binds
    abstract fun provideUseCase(impl: GetFeedPhotoDetailsUseCaseImpl): GetFeedPhotoDetailsUseCase
}