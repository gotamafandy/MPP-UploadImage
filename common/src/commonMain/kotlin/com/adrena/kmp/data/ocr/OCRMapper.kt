package com.adrena.kmp.data.ocr

import com.adrena.kmp.data.Mapper

class OCRMapper: Mapper<OCRResponse, OCR> {

    override fun transform(response: OCRResponse): OCR {
        return OCR(response.confidence, response.result)
    }
}