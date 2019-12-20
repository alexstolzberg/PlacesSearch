package com.stolz.placessearch.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.stolz.placessearch.database.PlaceDao

class SearchViewModelFactory(
    private val favoritesDatabase: PlaceDao
) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SearchViewModel::class.java)) {
            return SearchViewModel(favoritesDatabase) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}