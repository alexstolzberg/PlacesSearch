package com.stolz.placessearch.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Meta(
    val code: Int? = -1,
    val requestId: String? = ""
)