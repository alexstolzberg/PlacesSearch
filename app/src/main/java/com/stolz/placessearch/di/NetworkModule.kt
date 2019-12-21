package com.stolz.placessearch.di

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.stolz.placessearch.network.FoursquareApiService
import com.stolz.placessearch.network.GoogleMapsApiService
import com.stolz.placessearch.util.FOURSQUARE_BASE_URL
import com.stolz.placessearch.util.GOOGLE_BASE_URL
import dagger.Module
import dagger.Provides
import dagger.Reusable
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import javax.inject.Named

@Module
class NetworkModule {

    @Provides
    @Reusable
    internal fun providesMoshi(): Moshi {
        return Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
    }

    @Provides
    @Reusable
    @Named("google_retrofit")
    internal fun providesGoogleRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(GOOGLE_BASE_URL)
            .addConverterFactory(ScalarsConverterFactory.create())
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .build()
    }

    @Provides
    @Reusable
    @Named("foursquare_retrofit")
    internal fun providesFoursquareRetrofit(moshi: Moshi): Retrofit {
        return Retrofit.Builder()
            .baseUrl(FOURSQUARE_BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .build()
    }

    @Provides
    @Reusable
    internal fun providesGoogleMapsApiService(@Named("google_retrofit") retrofit: Retrofit): GoogleMapsApiService {
        return retrofit.create(GoogleMapsApiService::class.java)
    }

    @Provides
    @Reusable
    internal fun providesFourSquareApiService(@Named("foursquare_retrofit") retrofit: Retrofit): FoursquareApiService {
        return retrofit.create(FoursquareApiService::class.java)
    }
}
