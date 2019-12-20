package com.stolz.placessearch.model.venue_information

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Meta(
    val code: Int,
    val requestId: String
)