package com.adrena.kmp.data.ocr

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class OCRResponse(
    @SerialName("confidence") val confidence: Double,
    @SerialName("result") val result: String
)