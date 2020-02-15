package com.adrena.kmp.view

import com.badoo.reaktive.observable.Observable

interface ViewModelInput<R> {
    fun execute(request: R?)
}

interface ViewModelOutput<T> {
    val loading: Observable<Boolean>
    val result: Observable<T>
}

interface ViewModel<R, T> {
    val inputs: ViewModelInput<R>
    val outputs: ViewModelOutput<T>
}