package com.stolz.placessearch.model.places

import com.squareup.moshi.JsonClass
import com.stolz.placessearch.model.Category
import com.stolz.placessearch.model.Contact
import com.stolz.placessearch.model.Location

@JsonClass(generateAdapter = true)
data class Venue(val id: String = "",
                 val name: String = "",
                 val location: Location,
                 val categories: List<Category>,
                 val contact: Contact?,
                 val url: String? = "")