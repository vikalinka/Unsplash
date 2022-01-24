package lt.vitalikas.unsplash.di.modules

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import lt.vitalikas.unsplash.R

@Module
@InstallIn(SingletonComponent::class)
class SharedPrefsModule {

    @Provides
    fun provideContext(application: Application): Context = application

    @Provides
    fun provideSharedPrefs(context: Context): SharedPreferences =
        context.getSharedPreferences(
            context.getString(R.string.onboarding_prefs),
            Context.MODE_PRIVATE
        )
}