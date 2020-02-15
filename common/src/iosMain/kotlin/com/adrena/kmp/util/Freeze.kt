package com.adrena.kmp.util

import kotlin.native.concurrent.ensureNeverFrozen

actual fun Any.ensureNeverFrozen() {
    ensureNeverFrozen()
}