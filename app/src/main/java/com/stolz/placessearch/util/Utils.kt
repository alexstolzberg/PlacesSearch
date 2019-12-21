package com.stolz.placessearch.util

import android.app.Activity
import android.content.res.Resources
import android.util.DisplayMetrics
import android.util.Log
import android.view.inputmethod.InputMethodManager
import com.stolz.placessearch.R
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.roundToInt

private val TAG = Utils::class.java.simpleName

private const val STATIC_MAP_MARKER_FORMAT = "color:{COLOR}%7c{LATITUDE},{LONGITUDE}"

object Utils {

    enum class MarkerColor(val color: String) {
        RED("red"),
        GREEN("green")
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

    // TODO: JAVADOC
    fun generateStringFromLatLng(lat: Double, lng: Double): String {
        return "$lat,$lng"
    }


    // TODO: JAVADOC
    fun generateStaticMapDimensions(resources: Resources): String {
        val mapWidth = pxToDp(resources, Resources.getSystem().displayMetrics.widthPixels)
        val mapHeight = pxToDp(resources, resources.getDimension(R.dimen.app_bar_height).toInt())
        return mapWidth.toString() + "x" + mapHeight.toString()
    }

    // TODO: JAVADOC
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

        Log.d(
            TAG,
            "generateZoomLevel -- Distance from center: $distanceFromCenter / Zoom Level: $zoomLevel"
        )
        return zoomLevel
    }

    fun generateStaticMarkerQueryParam(lat: Double, lon: Double, markerColor: MarkerColor): String {
        return STATIC_MAP_MARKER_FORMAT
            .replace("{COLOR}", markerColor.color)
            .replace("{LATITUDE}", lat.toString())
            .replace("{LONGITUDE}", lon.toString())
    }

    /**
     * A helper function to convert pixels to dp
     */
    @JvmStatic
    fun pxToDp(resources: Resources, px: Int): Int =
        (px / (resources.displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT)).roundToInt()
}
