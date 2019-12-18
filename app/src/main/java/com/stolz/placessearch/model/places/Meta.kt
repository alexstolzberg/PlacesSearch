package com.stolz.placessearch.model.places

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Meta(
    val code: Int,
    val requestId: String
)