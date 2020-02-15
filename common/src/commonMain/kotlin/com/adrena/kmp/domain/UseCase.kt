package com.adrena.kmp.domain

import com.adrena.kmp.data.Result
import com.badoo.reaktive.single.Single

interface UseCase<in R, T> {
    fun execute(request: R?, forceCacheLoad: Boolean = false): Single<Result<T>>
}