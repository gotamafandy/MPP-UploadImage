package com.adrena.kmp.data.repository

import com.adrena.kmp.data.Result
import com.badoo.reaktive.single.Single

interface Repository<in R, T> {
    fun get(request: R?, forceCacheLoad: Boolean): Single<Result<T>>
}