package com.stolz.placessearch.network

import com.stolz.placessearch.BuildConfig
import com.stolz.placessearch.util.DEFAULT_ZOOM_LEVEL
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query


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
