package com.stolz.placessearch

import android.app.Activity
import android.view.inputmethod.InputMethodManager
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

    @JvmStatic
    fun hideSoftKeyboard(activity: Activity?) {
        if (activity == null) {
            return
        }
        val inputMethodManager =
            activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(activity.currentFocus?.windowToken, 0)
    }
}