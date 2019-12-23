package com.stolz.placessearch.util

import android.content.Context

import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader

object TestUtils {

    @Throws(Exception::class)
    private fun convertStreamToString(inputStream: InputStream): String {
        val reader = BufferedReader(InputStreamReader(inputStream))
        val sb = StringBuilder()
        reader.forEachLine { sb.append(it).append("\n") }
        reader.close()
        return sb.toString()
    }

    @Throws(Exception::class)
    fun getStringFromFile(context: Context, filePath: String): String {
        val stream = context.resources.assets.open(filePath)
        val result = convertStreamToString(stream)
        stream.close()
        return result
    }
}
