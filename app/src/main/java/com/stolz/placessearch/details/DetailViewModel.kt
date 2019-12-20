package com.stolz.placessearch.details

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.stolz.placessearch.model.Place
import com.stolz.placessearch.model.places.Venue
import com.stolz.placessearch.network.FoursquareApi
import com.stolz.placessearch.network.GoogleMapsApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import retrofit2.await
import java.io.BufferedInputStream
import java.io.ByteArrayInputStream


private val TAG = DetailViewModel::class.java.simpleName

class DetailViewModel : ViewModel() {

    // The detailed venue information
    private val _venueInformation = MutableLiveData<Venue>()
    val venueInformation: LiveData<Venue>
        get() = _venueInformation

    // The detailed venue information
    private val _staticMap = MutableLiveData<Bitmap>()
    val staticMap: LiveData<Bitmap>
        get() = _staticMap

    private var detailsJob = Job()
    private val detailsScope = CoroutineScope(detailsJob + Dispatchers.Main) // FIXME: THREADING!

    fun getStaticMap(place: Place) { // TODO: DAGGER IN CONTEXT
//        val mapWidth = Resources.getSystem().displayMetrics.widthPixels
//        val mapHeight = 300 // TODO: ADD DIMEN
        // TODO: PX TO DP

        val markerString = "color:red%7c" + place.location.latitude + "," + place.location.longitude

        detailsScope.launch {
            val staticMapDeferred = GoogleMapsApi.retrofitService.getStaticMap(
                placeMarker = markerString
            )
            try {
                val result = staticMapDeferred.await()
                val inputStream = result.byteStream()
                val bis = BufferedInputStream(inputStream)
                val bitmap = BitmapFactory.decodeStream(bis)
                _staticMap.value = bitmap
                Log.v(TAG, "Static map generated successfully")
                // TODO: CREATE BINDING ADAPTER TO SET IMAGE
            } catch (t: Throwable) {
                Log.e(TAG, "Static map generation failed - ${t.message}")
            }
        }
    }

    fun stringToBitmap(image: String): Bitmap? {
        return try {
            val encodeByte = Base64.decode(image, Base64.DEFAULT)

            val inputStream = ByteArrayInputStream(encodeByte)
            BitmapFactory.decodeStream(inputStream)
        } catch (e: Exception) {
            e.message
            null
        }

    }

    fun getVenueInformation(venueId: String) {
        detailsScope.launch {
            val venueInformationDeferred =
                FoursquareApi.retrofitService.getVenueDetails(venueId = venueId)
            try {
                val venueDeferred = venueInformationDeferred.await()
                val venue = venueDeferred.response.venue
                Log.v(TAG, "Got venue information successfully")
                _venueInformation.value = venue
            } catch (t: Throwable) {
                Log.e(TAG, "Failed retrieving venue information - ${t.message}")
            }
        }
    }

    fun favoriteToggled(place: Place) {
        // TODO: IMPLEMENT
    }

    override fun onCleared() {
        super.onCleared()
        detailsJob.cancel()
    }
}