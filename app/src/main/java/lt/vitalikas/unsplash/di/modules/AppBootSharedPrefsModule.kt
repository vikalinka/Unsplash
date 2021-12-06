package lt.vitalikas.unsplash.di.modules

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import lt.vitalikas.unsplash.data.repositories.AppBootSharedPrefsRepositoryImpl
import lt.vitalikas.unsplash.domain.repositories.AppBootSharedPrefsRepository

@Module
@InstallIn(ViewModelComponent::class)
abstract class AppBootSharedPrefsModule {

    @Binds
    abstract fun provideRepository(impl: AppBootSharedPrefsRepositoryImpl): AppBootSharedPrefsRepository
}