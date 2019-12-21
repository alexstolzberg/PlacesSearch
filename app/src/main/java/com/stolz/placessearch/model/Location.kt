package com.stolz.placessearch.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Location(
    val lat: Double = 0.0,
    val lng: Double = 0.0,
    val distance: Int = -1,
    val formattedAddress: List<String> = ArrayList(),
    val address: String = "",
    val crossStreet: String = "",
    val city: String = "",
    val state: String = "",
    val postalCode: String = "",
    val country: String = ""
)