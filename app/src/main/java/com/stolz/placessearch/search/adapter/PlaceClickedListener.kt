package com.stolz.placessearch.search.adapter

import com.stolz.placessearch.model.Place

/**
 * This interface passes back information to the fragment when a list item is clicked or favorited
 */
interface PlaceClickedListener {
    fun onPlaceClicked(clickedPlace: Place)
    fun onFavoriteClicked(clickedPlace: Place)
}