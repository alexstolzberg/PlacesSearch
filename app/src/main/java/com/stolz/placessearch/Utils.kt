package com.stolz.placessearch

import java.text.SimpleDateFormat
import java.util.*

object Utils {
    /**
     * Generates the query parameter for the Foursquare API requests based on the current data
     *
     * @return A formatted query parameter for the date
     */
    @JvmStatic
    fun generateDateString(): String {
        val formatter = SimpleDateFormat("YYYYMMdd", Locale.US)
        val currentDate = Date()
        return formatter.format(currentDate)
    }

    // TODO: JAVADOC
    @JvmStatic
    fun generateStringFromLatLng(lat: Double, lng: Double): String {
        return "$lat,$lng"
    }
}