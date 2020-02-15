package com.adrena.kmp.data

import com.badoo.reaktive.single.Single

interface Service<in R, T> {
    fun execute(request: R?): Single<Result<T>>
}