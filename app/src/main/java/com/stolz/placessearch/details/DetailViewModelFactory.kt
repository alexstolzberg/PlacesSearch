package com.stolz.placessearch.details

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.stolz.placessearch.database.PlaceDao

class DetailViewModelFactory(
    private val context: Context,
    private val favoritesDatabase: PlaceDao
) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DetailViewModel::class.java)) {
            return DetailViewModel(context, favoritesDatabase) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}