package com.stolz.placessearch.network

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.stolz.placessearch.SEATTLE_LATITUDE
import com.stolz.placessearch.SEATTLE_LONGITUDE
import com.stolz.placessearch.Utils
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

private const val BASE_URL = "https://maps.googleapis.com/maps/api/"

// TODO: MOVE THIS TO A MORE SECURE PLACE
private const val GOOGLE_MAPS_KEY = "AIzaSyDtOKOqWrTMcctBzkECJzwKPRXJt2LLClw"

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(ScalarsConverterFactory.create())
    .addCallAdapterFactory(CoroutineCallAdapterFactory())
    .baseUrl(BASE_URL)
    .build()

interface GoogleMapsApiService {

    // FIXME: MARKERS NOT ADDING TO STATIC MAP
    // FIXME: CLEAN UP
    @GET("staticmap")
    fun getStaticMap(
        @Query("center") center: String = "Seattle+Washington",
        @Query("zoom") zoom: Int = 15, // TODO: CALL GENERATE ZOOM AND PASS IN VALUE
        @Query("size") size: String = Utils.generateStringFromWidthAndHeight(300, 200),
        @Query("mapType") mapType: String = "roadmap",
        @Query("markers") centerMarker: String = "color:red%7c" + Utils.generateStringFromLatLng(
            SEATTLE_LATITUDE,
            SEATTLE_LONGITUDE
        ),
        @Query("markers") placeMarker: String,
        @Query("key") key: String = GOOGLE_MAPS_KEY
    ): Call<ResponseBody>
}

object GoogleMapsApi {
    val retrofitService: GoogleMapsApiService by lazy { retrofit.create(GoogleMapsApiService::class.java) }
}
