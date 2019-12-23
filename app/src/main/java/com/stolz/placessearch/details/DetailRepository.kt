package com.stolz.placessearch.details

import android.graphics.Bitmap
import android.util.Log
import com.stolz.placessearch.model.Place
import com.stolz.placessearch.model.places.Venue
import com.stolz.placessearch.network.FoursquareApiService
import com.stolz.placessearch.network.GoogleMapsApiService
import com.stolz.placessearch.util.BitmapUtils
import com.stolz.placessearch.util.SEATTLE_LATITUDE
import com.stolz.placessearch.util.SEATTLE_LONGITUDE
import com.stolz.placessearch.util.Utils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.await
import javax.inject.Inject

private val TAG = DetailRepository::class.java.simpleName

/**
 * This class handles all of the calls to the Foursquare and Google Maps APIs and returns the
 * information to the DetailViewModel.
 */
class DetailRepository @Inject constructor(
    private val foursquareApiService: FoursquareApiService,
    private val googleMapsApiService: GoogleMapsApiService,
    private val bitmapUtils: BitmapUtils
) {

    suspend fun fetchStaticMap(
        place: Place,
        dimensions: String = ""): Bitmap? {
        val centerMarkerString = Utils.generateStaticMarkerQueryParam(
            SEATTLE_LATITUDE,
            SEATTLE_LONGITUDE,
            Utils.MarkerColor.RED
        )
        val placeMarkerString = Utils.generateStaticMarkerQueryParam(
            place.location.latitude,
            place.location.longitude,
            Utils.MarkerColor.GREEN
        )

        return withContext(Dispatchers.IO) {
            val staticMapDeferred =
                googleMapsApiService.getStaticMap(
                    centerMarker = centerMarkerString,
                    placeMarker = placeMarkerString,
                    zoom = Utils.generateZoomLevel(place.distanceToCenter),
                    size = dimensions
                )
            try {
                val result = staticMapDeferred.await()
                Log.v(TAG, "Static map generated successfully")
                bitmapUtils.createBitmap(result)
            } catch (t: Throwable) {
                Log.e(TAG, "Static map generation failed - ${t.message}")
                null
            }
        }
    }

    suspend fun fetchVenueInformation(venueId: String): Venue? {
        return withContext(Dispatchers.IO) {
            val venueInformationDeferred =
                foursquareApiService.getVenueDetails(venueId = venueId)
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
}