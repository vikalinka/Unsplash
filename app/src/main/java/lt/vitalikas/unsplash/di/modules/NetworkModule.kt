package lt.vitalikas.unsplash.di.modules

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import lt.vitalikas.unsplash.data.apis.UnsplashApi
import lt.vitalikas.unsplash.data.networking.auth.AuthTokenInterceptor
import lt.vitalikas.unsplash.di.qualifiers.AuthTokenInterceptorQualifier
import lt.vitalikas.unsplash.di.qualifiers.LoggingInterceptorQualifier
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    @Provides
    @LoggingInterceptorQualifier
    fun provideLoginInterceptor(): Interceptor =
        HttpLoggingInterceptor()
            .setLevel(HttpLoggingInterceptor.Level.BODY)

    @Provides
    @AuthTokenInterceptorQualifier
    fun provideAuthTokenInterceptor(): Interceptor = AuthTokenInterceptor()

    @Provides
    fun provideOkHttpClient(
        @LoggingInterceptorQualifier loggingInterceptor: Interceptor,
        @AuthTokenInterceptorQualifier authTokenInterceptor: Interceptor
    ): OkHttpClient = OkHttpClient.Builder()
        .addNetworkInterceptor(loggingInterceptor)
        .addInterceptor(authTokenInterceptor)
        .followRedirects(true)
        .build()

    @Provides
    fun provideConverterFactory(): MoshiConverterFactory = MoshiConverterFactory.create()

    @Provides
    fun provideApi(
        okhttpClient: OkHttpClient,
        moshiConverterFactory: MoshiConverterFactory
    ): UnsplashApi {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.unsplash.com/")
            .addConverterFactory(moshiConverterFactory)
            .client(okhttpClient)
            .build()

        return retrofit.create<UnsplashApi>()
    }
}