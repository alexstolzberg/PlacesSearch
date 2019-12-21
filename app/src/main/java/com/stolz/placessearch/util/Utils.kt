package com.stolz.placessearch.util

import android.app.Activity
import android.content.res.Resources
import android.util.DisplayMetrics
import android.view.inputmethod.InputMethodManager
import com.stolz.placessearch.R
import java.lang.Long.parseLong
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.roundToInt

private const val STATIC_MAP_MARKER_FORMAT = "color:{COLOR}%7c{LATITUDE},{LONGITUDE}"

object Utils {

    enum class MarkerColor(val color: String) {
        RED("red"),
        GREEN("green")
    }

    /**
     * Hides the keyboard from the screen
     *
     * @param activity The current context
     */
    fun hideSoftKeyboard(activity: Activity?) {
        if (activity == null) {
            return
        }
        val inputMethodManager =
            activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(activity.currentFocus?.windowToken, 0)
    }

    /**
     * Generates the query parameter for the Foursquare API requests based on the current data
     *
     * @return A formatted query parameter for the date
     */
    fun generateDateString(): String {
        val formatter = SimpleDateFormat("YYYYMMdd", Locale.US)
        val currentDate = Date()
        return formatter.format(currentDate)
    }

    /**
     * Creates a string representation of a lat/lng object to be used for the Foursquare API
     *
     * @param lat the latitude of the place being passed in
     * @param lng the longitude of the place being passed in
     * @return A string representation of the lat/lng in the format "lat,lng"
     */
    fun generateStringFromLatLng(lat: Double, lng: Double): String {
        return "$lat,$lng"
    }

    /**
     * Generate appropriate dimensions for the static Google Map and return a string representation to be used in the query
     *
     * @param resources reference to Resources to allow for usage of dimens
     * @return A string representation of the lat/lng in the format "WidthxHeight"
     */
    fun generateStaticMapDimensions(resources: Resources): String {
        val mapWidth = pxToDp(resources, Resources.getSystem().displayMetrics.widthPixels)
        val mapHeight = pxToDp(resources, resources.getDimension(R.dimen.app_bar_height).toInt())
        return mapWidth.toString() + "x" + mapHeight.toString()
    }

    /**
     * Formats the phone number properly to display in the UI
     *
     * @param phoneNumber A phone number passed in as a string in the format "1234567890"
     * @return The formatted phone number in the format xxx-xxx-xxxx or "" if the format input was incorrect
     */
    fun formatPhoneNumber(phoneNumber: String?): String {
        if (phoneNumber.isNullOrEmpty()) {
            return ""
        }

        try {
            parseLong(phoneNumber)
        } catch (e: NumberFormatException) {
            // Check to see if the phone number is already formatted properly
            if (phoneNumber.length == 12) {
                val phoneNumberSplit = phoneNumber.split('-')
                if (phoneNumberSplit.size == 3
                    && phoneNumberSplit[0].length == 3
                    && phoneNumberSplit[1].length == 3
                    && phoneNumberSplit[2].length == 4
                ) {
                    // Phone number is already formatted
                    return phoneNumber
                }
            }
            return ""
        }

        if (phoneNumber.length != 10) {
            return ""
        }

        return phoneNumber.substring(0, 3) + "-" + phoneNumber.substring(
            3, 6
        ) + "-" + phoneNumber.substring(6, 10)
    }

    /**
     * Chooses an appropriate zoom level based on the distance between the center and the desired point
     *
     * @param distanceFromCenter The distance from the point to the center of the map
     * @return A zoom level appropriate to display both the desired point and the center of the map
     */
    fun generateZoomLevel(distanceFromCenter: Double): Int {
        var zoomLevel = DEFAULT_ZOOM_LEVEL

        if (distanceFromCenter <= 0.25) {
            zoomLevel = 15
        } else if (distanceFromCenter > 0.25 && distanceFromCenter <= 0.50) {
            zoomLevel = 14
        } else if (distanceFromCenter > 0.50 && distanceFromCenter <= 1.0) {
            zoomLevel = 13
        } else if (distanceFromCenter > 1.0 && distanceFromCenter <= 3.0) {
            zoomLevel = 12
        } else if (distanceFromCenter > 3.0 && distanceFromCenter <= 7.0) {
            zoomLevel = 10
        } else if (distanceFromCenter > 7.0 && distanceFromCenter <= 14.0) {
            zoomLevel = 9
        } else if (distanceFromCenter > 14.0 && distanceFromCenter <= 30.0) {
            zoomLevel = 8
        }

        return zoomLevel
    }

    /**
     * Generates a query param to add a marker tot he static Google Map API
     *
     * @param lat The latitude of the marker
     * @param lon The longitude of the marker
     * @param markerColor An enum object representing what color the marker should be
     * @return A string query used to add a marker to the static Google Map
     */
    fun generateStaticMarkerQueryParam(lat: Double, lon: Double, markerColor: MarkerColor): String {
        return STATIC_MAP_MARKER_FORMAT
            .replace("{COLOR}", markerColor.color)
            .replace("{LATITUDE}", lat.toString())
            .replace("{LONGITUDE}", lon.toString())
    }

    /**
     * A helper function to convert pixels to dp
     *
     * @param resources The resources object to get display metrics
     * @param px The value of pixels to convert to dp
     * @return The amount of dp
     */
    @JvmStatic
    fun pxToDp(resources: Resources, px: Int): Int =
        (px / (resources.displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT)).roundToInt()
}
