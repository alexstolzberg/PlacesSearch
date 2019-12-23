package com.stolz.placessearch.search

import android.util.Log
import com.google.android.gms.maps.model.LatLng
import com.stolz.placessearch.database.PlaceDao
import com.stolz.placessearch.model.Place
import com.stolz.placessearch.model.places.Venue
import com.stolz.placessearch.network.FoursquareApiService
import com.stolz.placessearch.util.NUM_METERS_PER_MILE
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.await
import java.text.DecimalFormat
import javax.inject.Inject

private val TAG = SearchRepository::class.java.simpleName

/**
 * This class handles all of the calls to the Foursquare API and returns the
 * information to the SearchViewModel.
 */
class SearchRepository @Inject constructor(
    private val foursquareApiService: FoursquareApiService,
    private val favoritesDatabase: PlaceDao
) {

    suspend fun fetchTypeaheadResults(query: String): Set<String> {
        return withContext(Dispatchers.IO) {
            try {
                val typeaheadResultsDeferred = foursquareApiService.getTypeaheadResults(
                    query = query
                )
                val result = typeaheadResultsDeferred.await()
                val minivenues = result.response.minivenues
                Log.v(TAG, "Typeahead search successful - ${minivenues.size} results")
                val suggestions: Set<String> = minivenues.map { it.name }.toSet()
                suggestions
            } catch (t: Throwable) {
                Log.e(TAG, "Typeahead search failed - ${t.message}")
                HashSet<String>()
            }
        }
    }

    suspend fun fetchPlaces(query: String): Set<Place>? {
        return withContext(Dispatchers.IO) {
            val searchPlacesDeferred = foursquareApiService.getPlaces(query = query)
            try {
                val result = searchPlacesDeferred.await()
                val venues = result.response.venues
                Log.v(TAG, "Places search successful - ${venues.size} venues retrieved")
                extractPlacesFromVenues(venues)
            } catch (t: Throwable) {
                Log.e(TAG, "Places search failed - ${t.message}")
                null
            }
        }
    }

    /**
     * This method takes the Venues model object and extracts places from it to return to the caller
     *
     * @param venues The model object to extract results from
     * @return A Set of place results -- a Set is used to filter out duplicates automatically
     */
    fun extractPlacesFromVenues(venues: List<Venue>): Set<Place> {
        val places = HashSet<Place>()
        for (venue in venues) {
            // Check the database to see if the current place has already been favorited
            val isFavorite = isVenueFavorited(venue.id)
            val id = venue.id
            val name = venue.name
            var categoryName = ""
            var iconUrl = ""
            if (venue.categories.isNotEmpty()) {
                val category = venue.categories[0]
                categoryName = category.name

                if (category.icon != null) {
                    val prefix = category.icon.prefix
                    val suffix = category.icon.suffix
                    iconUrl = prefix + "88" + suffix
                }
            }

            val loc = venue.location
            val location = LatLng(loc.lat, loc.lng)
            val distance = loc.distance / NUM_METERS_PER_MILE
            val numberFormat = DecimalFormat("#.00")
            val distanceFromCenter = java.lang.Double.valueOf(numberFormat.format(distance))
            val formattedAddress = loc.formattedAddress
            var address = ""
            if (formattedAddress.size >= 2) {
                address = formattedAddress[0] + "\n" + formattedAddress[1]
            }

            val p = Place(
                id = id,
                name = name,
                category = categoryName,
                address = address,
                location = location,
                distanceToCenter = distanceFromCenter,
                iconUrl = iconUrl,
                isFavorite = isFavorite
            )

            places.add(p)
        }

        return places
    }

    private fun isVenueFavorited(venueId: String): Boolean {
        val entity = favoritesDatabase.getPlace(venueId)
        return entity != null
    }
}