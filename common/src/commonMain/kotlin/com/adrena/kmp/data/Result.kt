package com.adrena.kmp.data

sealed class Result<T> {
    class Empty<T> : Result<T>()
    class Unauthorized<T> : Result<T>()

    data class Success<T>(val response: T) : Result<T>()
    data class Failure<T>(val errors: List<Error>?) : Result<T>()
    data class Exception<T>(val throwable: Throwable) : Result<T>()

    companion object {
        fun <T> success(response: T): Result<T> = Success(response)
        fun <T> exception(throwable: Throwable): Result<T> = Exception(throwable)
        fun <T> fail(errors: List<Error>?): Result<T> =
            Failure(errors)
        fun <T> empty(): Result<T> = Empty()
        fun <T> unauthorized(): Result<T> = Unauthorized()
    }
}