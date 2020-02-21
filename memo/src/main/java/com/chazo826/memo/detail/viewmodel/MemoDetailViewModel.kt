package com.chazo826.memo.detail.viewmodel

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.chazo826.core.dagger.extensions.NONE
import com.chazo826.core.dagger.extensions.isNotNone
import com.chazo826.core.dagger.viewmodel.StateBaseViewModel
import com.chazo826.data.memo.MemoRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject
import javax.inject.Named

class MemoDetailViewModel @Inject constructor(
    private val memoRepository: MemoRepository,
    @Named("memo_uid") val memoUid: Long
) : StateBaseViewModel() {

    private val _title = MutableLiveData<String>()
    val title: LiveData<String>
        get() = _title

    private val _contentHtml = MutableLiveData<String>()
    val contentHtml: LiveData<String>
        get() = _contentHtml

    private val _isEditable = MutableLiveData<Boolean>(false)
    val isEditable: Boolean
        get() = _isEditable.value == true

    var photoUri: Uri? = null

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

    fun setIsEditable(isEditable: Boolean) {
        if(isEditable != this.isEditable) {
            _isEditable.value = isEditable
        }
    }

    fun writeMemo(title: String, content: String) {
        disposable += when(memoUid) {
            Long.NONE -> memoRepository.insertMemo(title, content)
            else -> memoRepository.updateMemo(memoUid, title, content)
        }.stateTransformer()
            .subscribe({

            }, { Log.e(this::class.java.simpleName, it.toString()) })
    }

    fun deleteMemo() {
        disposable += memoRepository.deleteMemo(memoUid)
            .stateTransformer()
            .subscribe({

            }, { Log.e(this::class.java.simpleName, it.toString()) })
    }

    private fun fetchMemo(uid: Long) {
        disposable += memoRepository.fetchMemo(uid)
            .stateTransformer()
            .subscribe({
                _title.value = it.title
                _contentHtml.value = it.content
            }, { Log.e(this::class.java.simpleName, it.toString()) })
    }
}