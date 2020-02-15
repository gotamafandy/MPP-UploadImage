package com.adrena.kmp.util

import kotlinx.cinterop.readBytes
import platform.Foundation.NSData
import platform.Foundation.create

@ExperimentalUnsignedTypes
actual fun readFile(uri: String): ByteArray {
    val data = NSData.create(contentsOfFile = uri) ?: throw Exception("File not found!")
    return data.bytes?.readBytes(data.length.toInt()) ?: throw Exception("Read bytes error found!")
}