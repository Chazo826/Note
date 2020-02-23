package com.chazo826.core.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.CompletableTransformer
import io.reactivex.ObservableTransformer
import io.reactivex.SingleTransformer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

abstract class StateBaseViewModel : ViewModel() {

    protected val disposable by lazy { CompositeDisposable() }

    protected val _loadingState = MutableLiveData<Boolean>()
    val loadingState: LiveData<Boolean>
        get() = _loadingState

    fun <T> singleStateTransformer(): SingleTransformer<T, T> {
        return SingleTransformer {
            val single = it.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(singleLoadingTransformer())

            single
        }
    }

    private fun <T> singleLoadingTransformer(): SingleTransformer<T, T> {
        return SingleTransformer {
            val single = it.doOnSubscribe {
                _loadingState.postValue(true)
            }.doFinally {
                _loadingState.postValue(false)
            }
            single
        }
    }

    fun completableStateTransformer(): CompletableTransformer {
        return CompletableTransformer {
            val completable = it.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(completableLoadingTransformer())

            completable
        }
    }

    private fun completableLoadingTransformer(): CompletableTransformer {
        return CompletableTransformer {
            val completable = it.doOnSubscribe {
                _loadingState.postValue(true)
            }.doFinally {
                _loadingState.postValue(false)
            }
            completable
        }
    }

    fun <T> observableStateTransformer(): ObservableTransformer<T, T> {
        return ObservableTransformer {
            val observable = it.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(observableLoadingTransformer())

            observable
        }
    }

    private fun <T> observableLoadingTransformer(): ObservableTransformer<T, T> {
        return ObservableTransformer {
            val observable = it.doOnSubscribe {
                _loadingState.postValue(true)
            }.doFinally {
                _loadingState.postValue(false)
            }
            observable
        }
    }

    override fun onCleared() {
        disposable.clear()
        super.onCleared()
    }
}