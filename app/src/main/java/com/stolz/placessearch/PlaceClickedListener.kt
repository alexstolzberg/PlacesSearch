package com.stolz.placessearch

import com.stolz.placessearch.model.Place

interface PlaceClickedListener {
    fun onPlaceClicked(clickedPlace: Place)
}