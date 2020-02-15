package com.adrena.kmp.util

import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer
import okhttp3.logging.HttpLoggingInterceptor

actual val httpClient: HttpClient = HttpClient(OkHttp) {
    install(JsonFeature) {
        serializer = KotlinxSerializer()
    }
    engine {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        addInterceptor(loggingInterceptor)
    }
}