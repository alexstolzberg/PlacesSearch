package com.stolz.placessearch

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.stolz.placessearch.model.Object
import com.stolz.placessearch.network.FoursquareApi
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchViewModel : ViewModel() {

    // The internal MutableLiveData String that stores the most recent response
    private val _response = MutableLiveData<String>()

    // The external immutable LiveData for the response String
    val response: LiveData<String>
        get() = _response

    init {
        getPlaces()
        // TODO: REMOVE
    }


    private fun getPlaces() {
        FoursquareApi.retrofitService.getPlaces(query = "coffee") //FIXME: Feed in query
            .enqueue(object : Callback<Object> {
                override fun onFailure(call: Call<Object>, t: Throwable) {
                    _response.value = "Failure: " + t.message
                }

                override fun onResponse(call: Call<Object>, response: Response<Object>) {
                    val minivenues = response.body()?.response?.minivenues
                    _response.value = "Success, ${minivenues?.size} minivenues retrieved"
                }
            })
    }
}