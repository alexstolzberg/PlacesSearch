package com.stolz.placessearch

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.model.LatLng
import com.stolz.placessearch.model.Place
import com.stolz.placessearch.model.places.Venue
import com.stolz.placessearch.network.FoursquareApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.text.DecimalFormat

private val TAG = SearchViewModel::class.java.simpleName

class SearchViewModel : ViewModel() {

    enum class SearchStatus { LOADING, ERROR, DONE }

    private val _status =
        MutableLiveData<SearchStatus>()
    val status: LiveData<SearchStatus>
        get() = _status

    private val _venueImgUrl = MutableLiveData<String>()
    val venueImgUrl: LiveData<String>
        get() = _venueImgUrl

    // The list of places returned from the query
    private val _places = MutableLiveData<Set<Place>>()
    val places: LiveData<Set<Place>>
        get() = _places


    private var searchJob = Job()
    private val searchScope = CoroutineScope(searchJob + Dispatchers.Main)

    init {
        getPlaces("coffee")
        // TODO: REMOVE
    }

    private fun getTypeaheadResults(query: String) {
//        searchScope.launch {
//            val typeaheadResultsDeferred = FoursquareApi.retrofitService.getTypeaheadResults(query = query) //FIXME: Feed in query
//            try {
//                val result = typeaheadResultsDeferred.await()
//                val minivenues = result.response.minivenues
//                _status.value = "Success, ${minivenues.size} minivenues retrieved"
//
//                if (minivenues.isNotEmpty()) {
//                    val icon = minivenues[0].categories[0].icon
//
//                    _venueImgUrl.value = icon?.prefix.toString() + icon?.suffix.toString()
//                }
//            } catch (t: Throwable) {
//                _status.value = "Failure: " + t.message
//            }
//        }
    }

    private fun getPlaces(query: String) {
        searchScope.launch {
            val searchPlacesDeferred =
                FoursquareApi.retrofitService.getPlaces(query = query)
            try {
                _status.value = SearchStatus.LOADING
                val result = searchPlacesDeferred.await()
                _status.value = SearchStatus.DONE
                val venues = result.response.venues
                Log.v(TAG, "Places Search Successful - ${venues.size} venues retrieved")
                _places.value = extractPlacesFromVenues(venues)
            } catch (t: Throwable) {
                Log.e(TAG, "Places Search Successful - ${t.message}")
                _status.value = SearchStatus.ERROR
                _places.value = HashSet()
            }
        }
    }

    /**
     * This method takes the Venues model object and extracts places from it to return to the caller
     *
     * @param venues The model object to extract results from
     * @return A Set of place results -- a Set is used to filter out duplicates automatically
     */
    // TODO: SEE IF THERE IS A WAY TO EASILY MAP THE OBJECTS
    private fun extractPlacesFromVenues(venues: List<Venue>): Set<Place> {
        val places = HashSet<Place>()
        for (venue in venues) {
            // Check the database to see if the current place has already been favorited
            // TODO: FAVORITES FEATURE
            val isFavorite = false //isVenueFavorited(context, venue.getId())

            val id = venue.id
            val name = venue.name
            var categoryName = ""
            var iconUrl = ""
            if (venue.categories.isNotEmpty()) {
                val category = venue.categories[0]
                categoryName = category.name
                val categoryId = category.id // FIXME: REMOVE

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

    override fun onCleared() {
        super.onCleared()
        searchJob.cancel()
    }
}