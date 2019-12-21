package com.stolz.placessearch.network

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.stolz.placessearch.BuildConfig
import com.stolz.placessearch.util.DEFAULT_ZOOM_LEVEL
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

private const val BASE_URL = "https://maps.googleapis.com/maps/api/"

private val retrofit = Retrofit.Builder()
    .addConverterFactory(ScalarsConverterFactory.create())
    .addCallAdapterFactory(CoroutineCallAdapterFactory())
    .baseUrl(BASE_URL)
    .build()

interface GoogleMapsApiService {

    @GET("staticmap")
    fun getStaticMap(
        @Query("center") center: String = "Seattle+Washington",
        @Query("zoom") zoom: Int = DEFAULT_ZOOM_LEVEL,
        @Query("size") size: String = "300x200",
        @Query("mapType") mapType: String = "roadmap",
        @Query("markers", encoded = true) centerMarker: String = "",
        @Query("markers", encoded = true) placeMarker: String = "",
        @Query("key") key: String = BuildConfig.GOOGLE_MAPS_API_KEY
    ): Call<ResponseBody>
}

object GoogleMapsApi {
    val retrofitService: GoogleMapsApiService by lazy { retrofit.create(GoogleMapsApiService::class.java) }
}
