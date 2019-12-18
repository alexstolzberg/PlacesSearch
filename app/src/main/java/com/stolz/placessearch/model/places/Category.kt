package com.stolz.placessearch.model.places

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Category(
    val id: String,
    val name: String,
    val icon: Icon?
)