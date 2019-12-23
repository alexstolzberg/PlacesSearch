package com.stolz.placessearch.model.typeahead

import com.squareup.moshi.JsonClass
import com.stolz.placessearch.model.Category
import com.stolz.placessearch.model.Location

@JsonClass(generateAdapter = true)
data class Minivenue(
    val id: String = "",
    val name: String = "",
    val location: Location? = null,
    val categories: List<Category>? = null
)