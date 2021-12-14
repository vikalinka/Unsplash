package lt.vitalikas.unsplash.di.modules

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import lt.vitalikas.unsplash.data.repositories.AuthRepositoryImpl
import lt.vitalikas.unsplash.domain.repositories.AuthRepository

@Module
@InstallIn(ViewModelComponent::class)
abstract class AuthRepositoryModule {

    @Binds
    abstract fun provideRepository(impl: AuthRepositoryImpl): AuthRepository
}