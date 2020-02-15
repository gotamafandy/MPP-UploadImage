package com.adrena.kmp.view

import com.adrena.kmp.domain.UseCase
import com.adrena.kmp.data.Result
import com.badoo.reaktive.observable.*
import com.badoo.reaktive.subject.publish.PublishSubject

class ViewModelImpl<R, T>(useCase: UseCase<R, T>): ViewModel<R, T>, ViewModelInput<R>, ViewModelOutput<T> {
    override val inputs: ViewModelInput<R> = this
    override val outputs: ViewModelOutput<T> = this

    override val loading: Observable<Boolean>
    override val result: Observable<T>

    private val mStartProperty = PublishSubject<R?>()

    init {

        val loadingProperty = PublishSubject<Boolean>()

        loading = loadingProperty

        result = mStartProperty
            .doOnBeforeNext { loadingProperty.onNext(true) }
            .flatMapSingle {
                useCase.execute(it)
            }
            .flatMap {
                when (it) {
                    is Result.Success -> {
                        observableOf(it.response)
                    }
                    else -> observableOfEmpty()
                }
            }
            .doOnBeforeNext { loadingProperty.onNext(false) }
    }

    override fun execute(request: R?) {
        mStartProperty.onNext(request)
    }
}