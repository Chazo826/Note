package com.chazo826.memo.detail.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.chazo826.core.dagger.extensions.NONE
import com.chazo826.core.dagger.extensions.isNotNone
import com.chazo826.data.memo.MemoRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject
import javax.inject.Named

class MemoDetailViewModel @Inject constructor(
    private val memoRepository: MemoRepository,
    @Named("memo_uid") private val memoUid: Long
) : ViewModel() {
    private val disposable by lazy { CompositeDisposable() }

    private val _title = MutableLiveData<String>()
    val title: LiveData<String>
        get() = _title

    private val _contentHtml = MutableLiveData<String>()
    val contentHtml: LiveData<String>
        get() = _contentHtml

    private val _isEditable = MutableLiveData<Boolean>(false)
    val isEditable: Boolean
        get() = _isEditable.value == true

    val isDataExist: LiveData<Boolean> = MediatorLiveData<Boolean>().apply {
        val merge = {
            value = !title.value.isNullOrBlank() && !contentHtml.value.isNullOrEmpty()
        }

        addSource(title) { merge() }
        addSource(contentHtml) { merge() }
    }

    val menuInvalidate: LiveData<Boolean> = MediatorLiveData<Boolean>().apply {
        val onChange = {
            value = value != true
        }
        addSource(_isEditable) { onChange() }
        addSource(isDataExist) { onChange() }
    }


    init {
        if (memoUid.isNotNone()) {
            fetchMemo(memoUid)
        }
    }

    fun setIsEditable(isFocus: Boolean) {
        if(isFocus != isEditable) {
            _isEditable.value = isFocus
        }
    }

    fun writeMemo(title: String, content: String) {
        if (memoUid == Long.NONE) {
            memoRepository.insertMemo(title, content)
        } else {
            memoRepository.updateMemo(memoUid, title, content)
        }
    }

    private fun fetchMemo(uid: Long) {
        disposable += memoRepository.fetchMemo(uid)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                _title.value = it.title
                _contentHtml.value = it.content
            }, { Log.e(MemoDetailViewModel::class.java.simpleName, it.toString()) })
    }
}