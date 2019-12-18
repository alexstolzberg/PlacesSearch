package com.stolz.placessearch.model.typeahead

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Icon(
    val prefix: String? = "",
    val suffix: String? = ""
)