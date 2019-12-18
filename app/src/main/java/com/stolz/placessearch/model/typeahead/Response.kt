package com.stolz.placessearch.model.typeahead

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Response(val minivenues: List<Minivenue>)