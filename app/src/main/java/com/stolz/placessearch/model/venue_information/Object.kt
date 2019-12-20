package com.stolz.placessearch.model.venue_information

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Object(
    val meta: Meta,
    val response: Response
)