package lt.vitalikas.unsplash.di.modules

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import lt.vitalikas.unsplash.data.use_cases.GetProfileDataUseCaseImpl
import lt.vitalikas.unsplash.domain.use_cases.GetProfileDataUseCase

@Module
@InstallIn(ViewModelComponent::class)
abstract class GetProfileDataUseCaseModule {

    @Binds
    abstract fun provideUseCase(impl: GetProfileDataUseCaseImpl): GetProfileDataUseCase
}