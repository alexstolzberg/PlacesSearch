package com.stolz.placessearch.model.typeahead

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Category(
    val id: String = "",
    val name: String = "",
    val pluralName: String = "",
    val shortName: String = "",
    val icon: Icon?
)