package com.stolz.placessearch.model.typeahead

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Location(
    val address: String = "",
    val crossStreet: String = "",
    val city: String = "",
    val state: String = "",
    val postalCode: String = "",
    val country: String = "",
    val lat: Double = 0.0,
    val lng: Double = 0.0,
    val distance: Int = -1
)