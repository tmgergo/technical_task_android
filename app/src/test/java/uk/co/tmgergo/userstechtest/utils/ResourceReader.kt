package uk.co.tmgergo.userstechtest.utils

import java.io.IOException
import java.io.InputStreamReader
import java.nio.charset.StandardCharsets

object ResourceReader {
    fun readStringFromResource(assetFileName: String): String {
        try {
            val inputStream = this.javaClass.classLoader?.getResourceAsStream(assetFileName)

            val reader = InputStreamReader(inputStream, StandardCharsets.UTF_8)
            val builder = StringBuilder()
            reader.readLines().forEach {
                builder.append(it)
            }
            return builder.toString()
        } catch (e: IOException) {
            throw e
        }
    }
}
