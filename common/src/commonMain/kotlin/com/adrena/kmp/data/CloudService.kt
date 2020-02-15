package com.adrena.kmp.data

import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.header
import io.ktor.http.HttpHeaders
import io.ktor.http.takeFrom

abstract class CloudService<R, T>(private val mHost: String): Service<R, T> {
    fun HttpRequestBuilder.apiUrl(path: String? = null) {
        header(HttpHeaders.CacheControl, "no-cache")
        url {
            takeFrom(mHost)
            path?.let {
                encodedPath = it
            }
        }
    }
}