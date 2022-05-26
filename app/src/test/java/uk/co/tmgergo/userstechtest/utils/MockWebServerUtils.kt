package uk.co.tmgergo.userstechtest.utils

import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer

fun MockWebServer.enqueueResponseWithResourceBody(statusCode: Int, resourceFile: String) {
    val responseBody = ResourceReader.readStringFromResource(resourceFile)
    enqueue(
        MockResponse()
        .setResponseCode(statusCode)
        .setBody(responseBody)
    )
}

fun MockWebServer.enqueueResponse(statusCode: Int) {
    enqueue(
        MockResponse()
        .setResponseCode(statusCode)
    )
}
