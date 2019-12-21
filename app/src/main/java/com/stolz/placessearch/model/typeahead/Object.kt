package com.stolz.placessearch.model.typeahead

import com.squareup.moshi.JsonClass
import com.stolz.placessearch.model.Meta

@JsonClass(generateAdapter = true)
data class Object(
    val meta: Meta,
    val response: Response
)