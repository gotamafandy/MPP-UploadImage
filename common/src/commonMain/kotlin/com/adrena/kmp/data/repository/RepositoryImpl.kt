package com.adrena.kmp.data.repository

import com.adrena.kmp.data.Service
import com.adrena.kmp.data.Result
import com.badoo.reaktive.single.Single

class RepositoryImpl<R, T>(
    private val service: Service<R, T>,
    private val cacheService: Service<R, T>?): Repository<R, T> {

    override fun get(request: R?, forceCacheLoad: Boolean): Single<Result<T>> {
        return if (forceCacheLoad && cacheService != null) {
            cacheService.execute(request)
        } else {
            service.execute(request)
        }
    }
}