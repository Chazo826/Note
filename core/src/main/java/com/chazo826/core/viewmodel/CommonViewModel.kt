package com.chazo826.core.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.CompletableTransformer
import io.reactivex.SingleTransformer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

abstract class StateBaseViewModel : ViewModel() {

    protected val disposable by lazy { CompositeDisposable() }

    private val _loadingState = MutableLiveData<Boolean>()
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
            val single = it.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(completableLoadingTransformer())

            single
        }
    }

    private fun completableLoadingTransformer(): CompletableTransformer {
        return CompletableTransformer {
            val single = it.doOnSubscribe {
                _loadingState.postValue(true)
            }.doFinally {
                _loadingState.postValue(false)
            }
            single
        }
    }


}