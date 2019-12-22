package com.stolz.placessearch.search

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.stolz.placessearch.database.PlaceDao
import com.stolz.placessearch.model.Place
import com.stolz.placessearch.network.FoursquareApiService
import com.stolz.placessearch.util.FOURSQUARE_MIN_QUERY_LENGTH
import kotlinx.coroutines.*
import javax.inject.Inject

private val TAG = SearchViewModel::class.java.simpleName

class SearchViewModel @Inject constructor(
    private val favoritesDatabase: PlaceDao,
    foursquareApiService: FoursquareApiService
) : ViewModel() {

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

    var lastQuery: String = ""

    private var searchJob = Job()
    private val searchScope = CoroutineScope(searchJob + Dispatchers.Main)

    private val searchRepository = SearchRepository.getInstance()

    init{
        searchRepository.foursquareApiService = foursquareApiService
        searchRepository.favoritesDatabase = favoritesDatabase
    }

    fun getTypeaheadResults(query: String) {
        Log.v(TAG, "Getting typeahead results for query: \"${query}\"")

        if (query == lastQuery) {
            // If the query matches the last query one of the typeahead results was picked
            _typeaheadResults.value = HashSet()
            return
        }

        lastQuery = query

        if (query.length < FOURSQUARE_MIN_QUERY_LENGTH) {
            Log.w(TAG, "Query is not long enough to get type ahead suggestions")
            _typeaheadResults.value = HashSet()
            return
        }

        searchScope.launch {
            _typeaheadResults.value = searchRepository.fetchTypeaheadResults(query)
        }
    }

    fun getPlaces(query: String) {
        _typeaheadResults.value = HashSet()
        Log.v(TAG, "Getting places for query: \"${query}\"")
        searchScope.launch {
            _status.value = SearchStatus.LOADING
            val results = searchRepository.fetchPlaces(query)

            if (results == null) {
                _status.value = SearchStatus.ERROR
                _places.value = HashSet()
            } else {
                _places.value = results
                _status.value = if (results.isEmpty()) SearchStatus.EMPTY else SearchStatus.DONE
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

    override fun onCleared() {
        super.onCleared()
        searchJob.cancel()
    }
}