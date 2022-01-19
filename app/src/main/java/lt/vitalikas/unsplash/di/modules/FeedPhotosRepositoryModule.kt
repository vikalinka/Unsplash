package lt.vitalikas.unsplash.di.modules

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import lt.vitalikas.unsplash.data.repositories.FeedPhotosRepositoryImpl
import lt.vitalikas.unsplash.domain.repositories.FeedPhotosRepository

@Module
@InstallIn(ViewModelComponent::class)
abstract class FeedPhotosRepositoryModule {

    @Binds
    abstract fun provideRepository(impl: FeedPhotosRepositoryImpl): FeedPhotosRepository
}