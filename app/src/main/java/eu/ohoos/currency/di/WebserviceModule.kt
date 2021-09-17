package eu.ohoos.currency.di

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import eu.ohoos.currency.BuildConfig
import eu.ohoos.currency.common.constants.Constants
import eu.ohoos.currency.data.api.service.CurrencyService
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object WebserviceModule {

    @Singleton
    @Provides
    fun provideGsonBuilder(): Gson =
        GsonBuilder()
            .excludeFieldsWithoutExposeAnnotation()
            .create()

    @Singleton
    @Provides
    fun provideRetrofit(gson: Gson): Retrofit.Builder =
        Retrofit.Builder()
            .baseUrl(Constants.API_URL)
            .client(prepareHttpClientBuilder().build())
            .addConverterFactory(GsonConverterFactory.create(gson))

    @Singleton
    @Provides
    fun provideCurrencyService(retrofit: Retrofit.Builder): CurrencyService {
        val httpClient = prepareHttpClientBuilder()
        addLoggingInterceptor(httpClient)

        return retrofit.client(httpClient.build()).build().create(CurrencyService::class.java)
    }

    private fun prepareHttpClientBuilder(): OkHttpClient.Builder =
        getOkHttpBuilder()
            .addInterceptor(createHeaderInterceptor("Accept", "application/json"))

    private fun createHeaderInterceptor(key: String, value: String): Interceptor = Interceptor {
        var request = it.request()
        request = request.newBuilder()
            .addHeader(key, value)
            .build()

        it.proceed(request)
    }

    private fun addLoggingInterceptor(builder: OkHttpClient.Builder) {
        if (BuildConfig.DEBUG) {
            val interceptor = HttpLoggingInterceptor()
            interceptor.level = HttpLoggingInterceptor.Level.BODY
            builder.addInterceptor(interceptor)
        }
    }

    private fun getOkHttpBuilder(): OkHttpClient.Builder = OkHttpClient.Builder()

}