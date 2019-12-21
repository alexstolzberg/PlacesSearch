package com.stolz.placessearch.network

import com.stolz.placessearch.BuildConfig
import com.stolz.placessearch.model.places.Object
import com.stolz.placessearch.util.*
import kotlinx.coroutines.Deferred
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface FoursquareApiService {

    @GET("venues/suggestcompletion")
    fun getTypeaheadResults(
        @Query("client_id") clientId: String = BuildConfig.FOURSQUARE_CLIENT_ID,
        @Query("client_secret") clientSecret: String = BuildConfig.FOURSQUARE_CLIENT_SECRET,
        @Query("ll") latLng: String = Utils.generateStringFromLatLng(
            SEATTLE_LATITUDE,
            SEATTLE_LONGITUDE
        ),
        @Query("limit") limit: String = FOURSQUARE_MAX_TYPEAHEAD_RESULTS_LIMIT.toString(),
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
        @Query("limit") limit: String = FOURSQUARE_MAX_PLACE_RESULTS_LIMIT.toString(),
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
