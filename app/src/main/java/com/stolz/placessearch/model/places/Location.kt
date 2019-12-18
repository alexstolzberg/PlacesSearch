package com.stolz.placessearch.model.places

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Location(
    val lat: Double = 0.0,
    val lng: Double = 0.0,
    val distance: Int = -1,
    val formattedAddress: List<String>
)