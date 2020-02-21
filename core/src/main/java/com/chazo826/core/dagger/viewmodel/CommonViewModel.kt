package com.chazo826.core.dagger.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

abstract class StateBaseViewModel : ViewModel() {

    protected val disposable by lazy { CompositeDisposable() }

    private val _loadingState = MutableLiveData<Boolean>()
    val loadingState: LiveData<Boolean>
        get() = _loadingState

    fun <T> Single<T>.stateTransformer(): Single<T> {
        return compose {
            val single = it.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .loadingTransformer()

            single
        }
    }

    private fun <T> Single<T>.loadingTransformer(): Single<T> {
        return compose {
            val single = it.doOnSubscribe {
                _loadingState.postValue(true)
            }.doFinally {
                _loadingState.postValue(false)
            }
            single
        }
    }

    fun Completable.stateTransformer(): Completable {
        return compose {
            val single = it.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .loadingTransformer()

            single
        }
    }

    private fun Completable.loadingTransformer(): Completable {
        return compose {
            val single = it.doOnSubscribe {
                _loadingState.postValue(true)
            }.doFinally {
                _loadingState.postValue(false)
            }
            single
        }
    }


}