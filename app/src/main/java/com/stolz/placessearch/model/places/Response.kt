package com.stolz.placessearch.model.places

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Response(val venues: List<Venue>)