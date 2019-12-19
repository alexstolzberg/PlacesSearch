package com.stolz.placessearch

import android.app.Activity
import android.content.res.Resources
import android.util.DisplayMetrics
import android.view.inputmethod.InputMethodManager
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.roundToInt

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

    /**
     * A helper function to convert pixels to dp
     */
    @JvmStatic
    fun pxToDp(resources: Resources, px: Int): Int {
        val displayMetrics = resources.displayMetrics
        return (px / (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT)).roundToInt()
    }
}