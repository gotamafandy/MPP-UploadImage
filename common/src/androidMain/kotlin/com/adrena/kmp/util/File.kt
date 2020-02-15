package com.adrena.kmp.util

import java.io.File

actual fun readFile(uri: String): ByteArray = File(uri).readBytes()
