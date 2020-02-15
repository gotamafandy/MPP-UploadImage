package com.adrena.kmp.domain

import com.adrena.kmp.data.repository.Repository
import com.adrena.kmp.data.Result
import com.badoo.reaktive.single.Single

class UseCaseImpl<R, T>(
    private val repository: Repository<R, T>
): UseCase<R, T> {

    override fun execute(request: R?, forceCacheLoad: Boolean): Single<Result<T>> {
        return repository.get(request, forceCacheLoad)
    }
}