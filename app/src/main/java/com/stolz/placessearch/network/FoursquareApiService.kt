package com.stolz.placessearch.network

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.stolz.placessearch.BuildConfig
import com.stolz.placessearch.model.places.Object
import com.stolz.placessearch.util.SEATTLE_LATITUDE
import com.stolz.placessearch.util.SEATTLE_LONGITUDE
import com.stolz.placessearch.util.Utils
import kotlinx.coroutines.Deferred
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

private const val BASE_URL = "https://api.foursquare.com/v2/"
private const val FOURSQUARE_DEFAULT_LIMIT = 50

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .addCallAdapterFactory(CoroutineCallAdapterFactory())
    .baseUrl(BASE_URL)
    .build()

interface FoursquareApiService {

    companion object {
        const val FOURSQUARE_MIN_QUERY_LENGTH = 3
    }

    @GET("venues/suggestcompletion")
    fun getTypeaheadResults(
        @Query("client_id") clientId: String = BuildConfig.FOURSQUARE_CLIENT_ID,
        @Query("client_secret") clientSecret: String = BuildConfig.FOURSQUARE_CLIENT_SECRET,
        @Query("ll") latLng: String = Utils.generateStringFromLatLng(
            SEATTLE_LATITUDE,
            SEATTLE_LONGITUDE
        ),
        @Query("limit") limit: String = FOURSQUARE_DEFAULT_LIMIT.toString(),
        @Query("query") query: String,
        @Query("v") timestamp: String = Utils.generateDateString()
    ): Deferred<com.stolz.placessearch.model.typeahead.Object>

    @GET("venues/search")
    fun getPlaces(
        @Query("client_id") clientId: String = BuildConfig.FOURSQUARE_CLIENT_ID,
        @Query("client_secret") clientSecret: String = BuildConfig.FOURSQUARE_CLIENT_SECRET,
        @Query("ll") latLng: String = Utils.generateStringFromLatLng(
            SEATTLE_LATITUDE,
            SEATTLE_LONGITUDE
        ),
        @Query("limit") limit: String = FOURSQUARE_DEFAULT_LIMIT.toString(),
        @Query("query") query: String,
        @Query("v") timestamp: String = Utils.generateDateString()
    ): Deferred<Object>

    @GET("venues/{venueId}")
    fun getVenueDetails(
        @Path("venueId") venueId: String,
        @Query("client_id") clientId: String = BuildConfig.FOURSQUARE_CLIENT_ID,
        @Query("client_secret") clientSecret: String = BuildConfig.FOURSQUARE_CLIENT_SECRET,
        @Query("v") timestamp: String = Utils.generateDateString()
    ): Deferred<com.stolz.placessearch.model.venue_information.Object>
}

object FoursquareApi {
    val retrofitService: FoursquareApiService by lazy { retrofit.create(FoursquareApiService::class.java) }
}
