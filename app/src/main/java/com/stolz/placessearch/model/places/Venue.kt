package com.stolz.placessearch.model.places

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Venue(val id: String = "",
                 val name: String = "",
                 val location: Location,
                 val categories: List<Category>,
                 val contact: Contact?,
                 val url: String? = "")