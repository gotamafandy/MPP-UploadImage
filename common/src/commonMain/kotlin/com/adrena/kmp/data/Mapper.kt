package com.adrena.kmp.data

interface Mapper<in T, out E> {
    fun transform(response: T): E
}