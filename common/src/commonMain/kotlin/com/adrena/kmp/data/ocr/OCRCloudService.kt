package com.adrena.kmp.data.ocr

import com.adrena.kmp.coroutinesinterop.singleFromCoroutine
import com.adrena.kmp.data.CloudService
import com.adrena.kmp.data.Mapper
import com.adrena.kmp.data.Result
import com.adrena.kmp.util.httpClient
import com.adrena.kmp.util.readFile
import com.badoo.reaktive.single.Single
import com.badoo.reaktive.single.single
import io.ktor.client.request.forms.MultiPartFormDataContent
import io.ktor.client.request.forms.formData
import io.ktor.client.request.post
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.readText
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import kotlinx.serialization.UnstableDefault
import kotlinx.serialization.json.Json

class OCRCloudService<T>(host: String, private val mapper: Mapper<OCRResponse, T>): CloudService<String, T>(host) {

    @UseExperimental(UnstableDefault::class)
    override fun execute(request: String?): Single<Result<T>> {
        val uri = request ?: return single { Result.exception<T>(Throwable("Request is empty")) }

        return singleFromCoroutine {
            val bytes = readFile(uri)

            val httpResponse = httpClient.post<HttpResponse> {
                apiUrl()
                body = MultiPartFormDataContent(
                    formData {
                        append(
                            "file",
                            bytes,
                            Headers.build {
                                append(HttpHeaders.ContentType, "image/jpg")
                                append(HttpHeaders.ContentDisposition, "filename=image.jpg")
                            })
                    }
                )
            }

            val json = httpResponse.readText()

            val response = Json.nonstrict.parse(OCRResponse.serializer(), json)

            Result.success(mapper.transform(response))
        }
    }
}