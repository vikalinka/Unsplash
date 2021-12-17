package lt.vitalikas.unsplash.di.modules

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import lt.vitalikas.unsplash.data.repositories.ProfileRepositoryImpl
import lt.vitalikas.unsplash.domain.repositories.ProfileRepository

@Module
@InstallIn(ViewModelComponent::class)
abstract class ProfileRepositoryModule {

//    @Provides
//    fun provideRepository(impl: ProfileRepositoryImpl): ProfileRepository {
//        return impl
//    }

    @Binds
    abstract fun provideRepository(impl: ProfileRepositoryImpl): ProfileRepository
}