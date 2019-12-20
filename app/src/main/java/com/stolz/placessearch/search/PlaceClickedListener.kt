package com.stolz.placessearch.search

import com.stolz.placessearch.model.Place

interface PlaceClickedListener {
    fun onPlaceClicked(clickedPlace: Place)
    fun onFavoriteClicked(clickedPlace: Place)
}