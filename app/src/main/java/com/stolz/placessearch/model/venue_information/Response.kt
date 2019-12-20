package com.stolz.placessearch.model.venue_information

import com.squareup.moshi.JsonClass
import com.stolz.placessearch.model.places.Venue

@JsonClass(generateAdapter = true)
data class Response(val venue: Venue)