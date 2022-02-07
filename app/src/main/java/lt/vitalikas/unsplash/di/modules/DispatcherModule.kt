package lt.vitalikas.unsplash.di.modules

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import lt.vitalikas.unsplash.di.qualifiers.DispatcherIoQualifier

@Module
@InstallIn(SingletonComponent::class)
class DispatcherModule {

    @Provides
    @DispatcherIoQualifier
    fun provideDispatcherIo(): CoroutineDispatcher = Dispatchers.IO

    @Provides
    fun provideDispatcher(
        @DispatcherIoQualifier dispatcherIo: CoroutineDispatcher
    ): CoroutineDispatcher = dispatcherIo
}