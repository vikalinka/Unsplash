package lt.vitalikas.unsplash.di.modules

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import lt.vitalikas.unsplash.data.prefsstore.PrefsStore
import lt.vitalikas.unsplash.data.prefsstore.PrefsStoreImpl

@Module
@InstallIn(SingletonComponent::class)
class DataStoreModule {

    @Provides
    fun provideDataStore(impl: PrefsStoreImpl): PrefsStore = impl
}
