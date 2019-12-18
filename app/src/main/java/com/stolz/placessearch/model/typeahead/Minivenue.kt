package com.stolz.placessearch.model.typeahead

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Minivenue(
    val id: String,
    val name: String,
    val location: Location,
    val categories: List<Category>
)