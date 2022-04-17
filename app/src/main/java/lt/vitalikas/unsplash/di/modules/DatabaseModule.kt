package lt.vitalikas.unsplash.di.modules

import android.app.Application
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import lt.vitalikas.unsplash.data.db.Db

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {

    @Provides
    fun provideDatabase(context: Application): Db = Db.getInstance(context)
}