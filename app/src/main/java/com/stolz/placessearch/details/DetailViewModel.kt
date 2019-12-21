package com.stolz.placessearch.details

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.stolz.placessearch.database.PlaceDao
import com.stolz.placessearch.model.Place
import com.stolz.placessearch.model.places.Venue
import com.stolz.placessearch.network.FoursquareApi
import com.stolz.placessearch.network.GoogleMapsApi
import com.stolz.placessearch.util.SEATTLE_LATITUDE
import com.stolz.placessearch.util.SEATTLE_LONGITUDE
import com.stolz.placessearch.util.Utils
import kotlinx.coroutines.*
import okhttp3.ResponseBody
import retrofit2.await
import java.io.BufferedInputStream

private val TAG = DetailViewModel::class.java.simpleName

class DetailViewModel(
    private val context: Context,
    private val favoritesDatabase: PlaceDao
) : ViewModel() {

    // The detailed venue information
    private val _venueInformation = MutableLiveData<Venue>()
    val venueInformation: LiveData<Venue>
        get() = _venueInformation

    // The static map
    private val _staticMap = MutableLiveData<Bitmap>()
    val staticMap: LiveData<Bitmap>
        get() = _staticMap

    private var detailsJob = Job()
    private val detailsScope = CoroutineScope(detailsJob + Dispatchers.Main)

    fun getStaticMap(place: Place) { // TODO: DAGGER IN CONTEXT
        detailsScope.launch {
            val bitmap = fetchStaticMap(place)
            if (bitmap != null) {
                _staticMap.value = bitmap
            }
        }
    }

    private suspend fun fetchStaticMap(place: Place): Bitmap? {
        val centerMarkerString = Utils.generateStaticMarkerQueryParam(SEATTLE_LATITUDE, SEATTLE_LONGITUDE, Utils.MarkerColor.RED)
        val placeMarkerString = Utils.generateStaticMarkerQueryParam(place.location.latitude, place.location.longitude, Utils.MarkerColor.GREEN)

        return withContext(Dispatchers.IO) {
            val staticMapDeferred =
                GoogleMapsApi.retrofitService.getStaticMap(
                    centerMarker = centerMarkerString,
                    placeMarker = placeMarkerString,
                    zoom = Utils.generateZoomLevel(place.distanceToCenter),
                    size = Utils.generateStaticMapDimensions(context.resources)
                )
            try {
                val result = staticMapDeferred.await()
                Log.v(TAG, "Static map generated successfully")
                createBitmap(result)
            } catch (t: Throwable) {
                Log.e(TAG, "Static map generation failed - ${t.message}")
                null
            }
        }
    }

    fun getVenueInformation(venueId: String) {
        detailsScope.launch {
            val venueWithInfo = fetchVenueInformation(venueId)
            if (venueWithInfo != null) {
                _venueInformation.value = venueWithInfo
            }
        }
    }

    private suspend fun fetchVenueInformation(venueId: String): Venue? {
        return withContext(Dispatchers.IO) {
            val venueInformationDeferred =
                FoursquareApi.retrofitService.getVenueDetails(venueId = venueId)
            try {
                val venueDeferred = venueInformationDeferred.await()
                Log.v(TAG, "Got venue information successfully")
                venueDeferred.response.venue
            } catch (t: Throwable) {
                Log.e(TAG, "Failed retrieving venue information - ${t.message}")
                null
            }
        }
    }

    fun updateFavoriteForPlace(place: Place) {
        detailsScope.launch {
            withContext(Dispatchers.IO) {
                val placeEntity = Place.toPlaceEntity(place)
                val placeInDb = favoritesDatabase.getPlace(place.id)
                if (placeInDb == null) {
                    favoritesDatabase.insert(placeEntity)
                } else {
                    favoritesDatabase.updatePlace(placeEntity)

                }
            }
        }
    }

    private fun createBitmap(responseBody: ResponseBody): Bitmap {
        val inputStream = responseBody.byteStream()
        val bis = BufferedInputStream(inputStream)
        return BitmapFactory.decodeStream(bis)
    }

    override fun onCleared() {
        super.onCleared()
        detailsJob.cancel()
    }
}