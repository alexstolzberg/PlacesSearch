package com.stolz.placessearch.search

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.model.LatLng
import com.stolz.placessearch.database.PlaceDao
import com.stolz.placessearch.model.Place
import com.stolz.placessearch.model.places.Venue
import com.stolz.placessearch.model.typeahead.Minivenue
import com.stolz.placessearch.network.FoursquareApi
import com.stolz.placessearch.network.FoursquareApiService
import com.stolz.placessearch.util.NUM_METERS_PER_MILE
import kotlinx.coroutines.*
import java.text.DecimalFormat

private val TAG = SearchViewModel::class.java.simpleName

class SearchViewModel(private val favoritesDatabase: PlaceDao) : ViewModel() {

    enum class SearchStatus { LOADING, ERROR, DONE, EMPTY }

    // The list of places returned from the query
    private val _typeaheadResults = MutableLiveData<Set<String>>()
    val typeaheadResults: LiveData<Set<String>>
        get() = _typeaheadResults

    // The status of loading places
    private val _status =
        MutableLiveData<SearchStatus>()
    val status: LiveData<SearchStatus>
        get() = _status

    // The list of places returned from the query
    private val _places = MutableLiveData<Set<Place>>()
    val places: LiveData<Set<Place>>
        get() = _places

    // TODO: LiveData?
    private var lastQuery: String = ""

    private var searchJob = Job()
    private val searchScope = CoroutineScope(searchJob + Dispatchers.Main)

    fun queryUpdated(query: String) {
        lastQuery = query
    }

    fun getTypeaheadResults(query: String) {
        Log.v(TAG, "Getting typeahead results for query: \"${query}\"")

        if (query == lastQuery) {
            // If the query matches the last query one of the typeahead results was picked
            _typeaheadResults.value = HashSet()
            return
        }

        lastQuery = query

        if (query.length < FoursquareApiService.FOURSQUARE_MIN_QUERY_LENGTH) {
            Log.w(TAG, "Query is not long enough to get type ahead suggestions")
            return
        }

        _places.value = HashSet()
        _status.value = SearchStatus.EMPTY
        searchScope.launch {
            _typeaheadResults.value = fetchTypeaheadResults(query)
        }
    }

    private suspend fun fetchTypeaheadResults(query: String): Set<String> {
        return withContext(Dispatchers.IO) {
            try {
                val typeaheadResultsDeferred = FoursquareApi.retrofitService.getTypeaheadResults(
                    query = query
                )
                val result = typeaheadResultsDeferred.await()
                val minivenues = result.response.minivenues
                Log.v(TAG, "Typeahead search successful - ${minivenues.size} results")
                extractSuggestionsFromMinivenues(minivenues)
            } catch (t: Throwable) {
                Log.e(TAG, "Typeahead search failed - ${t.message}")
                HashSet<String>()
            }
        }
    }

    fun getPlaces(query: String) {
        _typeaheadResults.value = HashSet()
        Log.v(TAG, "Getting places for query: \"${query}\"")
        searchScope.launch {
            _status.value = SearchStatus.LOADING
            val results = fetchPlaces(query)
            _places.value = results
            _status.value = if (results.isEmpty()) SearchStatus.EMPTY else SearchStatus.DONE

        }
    }

    private suspend fun fetchPlaces(query: String): Set<Place> {
        return withContext(Dispatchers.IO) {
            val searchPlacesDeferred = FoursquareApi.retrofitService.getPlaces(query = query)
            try {
                val result = searchPlacesDeferred.await()
                val venues = result.response.venues
                Log.v(TAG, "Places search successful - ${venues.size} venues retrieved")
                extractPlacesFromVenues(venues)
            } catch (t: Throwable) {
                Log.e(TAG, "Places search failed - ${t.message}")
                HashSet<Place>()
            }
        }
    }

    fun updateFavoriteForPlace(place: Place) {
        searchScope.launch {
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

    /**
     * This method takes the Minivenues model object and extracts type ahead suggestions from it to
     * return to the caller
     *
     * @param minivenues The model object to extract results from
     * @return A Set of type ahead search results (a Set is used to automatically handle duplicates)
     */
    // TODO: SEE IF THERE IS A WAY TO EASILY MAP THE OBJECTS
    private fun extractSuggestionsFromMinivenues(minivenues: List<Minivenue>): Set<String> {
        val typeAheadSuggestions = java.util.HashSet<String>()
        for (minivenue in minivenues) {
            val minivenueName = minivenue.name
            typeAheadSuggestions.add(minivenueName)
        }
        return typeAheadSuggestions
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

    override fun onCleared() {
        super.onCleared()
        searchJob.cancel()
    }
}