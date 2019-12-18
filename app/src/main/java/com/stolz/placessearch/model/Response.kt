package com.stolz.placessearch.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Response(val minivenues: List<Minivenue>)