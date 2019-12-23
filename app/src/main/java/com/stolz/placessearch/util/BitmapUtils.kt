package com.stolz.placessearch.util

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import okhttp3.ResponseBody
import java.io.BufferedInputStream
import javax.inject.Inject

class BitmapUtils @Inject constructor() {
    fun createBitmap(responseBody: ResponseBody): Bitmap {
        val inputStream = responseBody.byteStream()
        val bis = BufferedInputStream(inputStream)
        return BitmapFactory.decodeStream(bis)
    }
}