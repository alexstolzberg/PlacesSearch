package com.stolz.placessearch

import android.app.Activity
import android.content.res.Resources
import android.util.DisplayMetrics
import android.view.inputmethod.InputMethodManager
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.roundToInt

object Utils {

    enum class MarkerColor { RED, GREEN }

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


    // TODO: JAVADOC
    @JvmStatic
    fun generateStringFromWidthAndHeight(width: Int, height: Int): String {
        return width.toString() + "x" + height.toString()
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
     * Formats the phone number properly to display in the UI
     */
    @JvmStatic
    fun formatPhoneNumber(phoneNumber: String?): String {
        if (phoneNumber.isNullOrEmpty() || phoneNumber.length != 10) {
            return ""
        }

        return phoneNumber.substring(0, 3) + "-" + phoneNumber.substring(
            3,
            6
        ) + "-" + phoneNumber.substring(6, 10)
    }

    /**
     * Chooses an appropriate zoom level based on the distance between the center and the desired point
     *
     * @param distanceFromCenter The distance from the point to the center of the map
     * @return A zoom level appropriate to display both the desired point and the center of the map
     */
    @JvmStatic
    fun generateZoomLevel(distanceFromCenter: Double): Int {
        if (distanceFromCenter <= 0.25) {
            return 15
        } else if (distanceFromCenter > 0.25 && distanceFromCenter <= 0.50) {
            return 14
        } else if (distanceFromCenter > 0.50 && distanceFromCenter <= 1.0) {
            return 13
        } else if (distanceFromCenter > 1.0 && distanceFromCenter <= 3.0) {
            return 12
        } else if (distanceFromCenter > 3.0 && distanceFromCenter <= 7.0) {
            return 10
        } else if (distanceFromCenter > 7.0 && distanceFromCenter <= 14.0) {
            return 9
        } else if (distanceFromCenter > 14.0 && distanceFromCenter <= 30.0) {
            return 8
        }
        return 7
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