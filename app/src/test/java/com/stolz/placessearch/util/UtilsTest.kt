package com.stolz.placessearch.util

import junit.framework.Assert.assertEquals
import org.junit.Test

class UtilsTest {

    @Test
    fun test_generateStringFromLatLng() {
        val expected = "1.23,4.56"
        val result = Utils.generateStringFromLatLng(1.23, 4.56)
        assertEquals("Output does not match", expected, result)
    }

    @Test
    fun test_formatPhoneNumber_null() {
        val expected = ""
        val result = Utils.formatPhoneNumber(null)
        assertEquals("Output does not match", expected, result)
    }

    @Test
    fun test_formatPhoneNumber_empty() {
        val expected = ""
        val result = Utils.formatPhoneNumber("")
        assertEquals("Output does not match", expected, result)
    }

    @Test
    fun test_formatPhoneNumber_wrongLength() {
        val expected = ""
        val result = Utils.formatPhoneNumber("123")
        assertEquals("Output does not match", expected, result)
    }

    @Test
    fun test_formatPhoneNumber_valid() {
        val expected = "123-456-7890"
        val result = Utils.formatPhoneNumber("1234567890")
        assertEquals("Output does not match", expected, result)
    }

    @Test
    fun test_generateZoomLevel() {
        var result = Utils.generateZoomLevel(.25)
        assertEquals("Output does not match", 15, result)

        result = Utils.generateZoomLevel(.26)
        assertEquals("Output does not match", 14, result)

        result = Utils.generateZoomLevel(.51)
        assertEquals("Output does not match", 13, result)

        result = Utils.generateZoomLevel(1.1)
        assertEquals("Output does not match", 12, result)

        result = Utils.generateZoomLevel(3.1)
        assertEquals("Output does not match", 10, result)

        result = Utils.generateZoomLevel(7.1)
        assertEquals("Output does not match", 9, result)

        result = Utils.generateZoomLevel(14.1)
        assertEquals("Output does not match", 8, result)

        result = Utils.generateZoomLevel(31.0)
        assertEquals("Output does not match", 7, result)
    }

    @Test
    fun test_generateStaticMarkerQueryParam() {
        val expected = "color:red%7c1.23,4.56"
        val result = Utils.generateStaticMarkerQueryParam(1.23, 4.56, Utils.MarkerColor.RED)
        assertEquals("Output does not match", expected, result)
    }
}