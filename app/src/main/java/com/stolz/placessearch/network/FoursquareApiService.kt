package com.stolz.placessearch.network

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.stolz.placessearch.SEATTLE_LATITUDE
import com.stolz.placessearch.SEATTLE_LONGITUDE
import com.stolz.placessearch.Utils
import com.stolz.placessearch.model.Object
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

private const val BASE_URL = "https://api.foursquare.com/v2/"
private const val FOURSQUARE_DEFAULT_LIMIT =
    50 // TODO: Limited results to 50 per request -- add customization to this field

// TODO: MOVE THESE TO A MORE SECURE PLACE
private const val CLIENT_ID = "WD3UQZ03SJ4VDZSDEWXBHPSOHZSNRGQIBAEY3Q0UGWPXXYJH"
private const val CLIENT_SECRET = "L2NUD1USBGSCRLZF1WVA4PEE0VD2PPAR01RF3IJ5QB4BABCD"

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .build()

interface FoursquareApiService {

    @GET("venues/suggestcompletion")
    fun getPlaces(
        @Query("client_id") clientId: String = CLIENT_ID,
        @Query("client_secret") clientSecret: String = CLIENT_SECRET,
        @Query("ll") latLng: String = Utils.generateStringFromLatLng(
            SEATTLE_LATITUDE,
            SEATTLE_LONGITUDE
        ),
        @Query("limit") limit: String = FOURSQUARE_DEFAULT_LIMIT.toString(),
        @Query("query") query: String,
        @Query("v") timestamp: String = Utils.generateDateString()
    ): Call<Object>
}

object FoursquareApi {
    val retrofitService: FoursquareApiService by lazy { retrofit.create(FoursquareApiService::class.java) }
}