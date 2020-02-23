package com.chazo826.memo.detail.viewmodel

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.chazo826.core.extensions.NONE
import com.chazo826.core.extensions.isNotNone
import com.chazo826.core.viewmodel.StateBaseViewModel
import com.chazo826.data.memo.MemoRepository
import io.reactivex.rxkotlin.plusAssign
import javax.inject.Inject
import javax.inject.Named

class MemoDetailViewModel @Inject constructor(
    private val memoRepository: MemoRepository,
    @Named("memo_uid") var memoUid: Long
) : StateBaseViewModel() {

    val title = MutableLiveData<String>()

    val content = MutableLiveData<String>()

    val savedImageUris = mutableListOf<Uri>()

    private val _isEditable = MutableLiveData<Boolean>(false)
    val isEditable: Boolean
        get() = _isEditable.value == true

    private val _imageUris = MutableLiveData<List<Uri>>()
    val imageUris: LiveData<List<Uri>>
        get() = _imageUris

    var photoUri: Uri? = null

    val isDataExist: MediatorLiveData<Boolean> = MediatorLiveData<Boolean>().apply {
        val merge = {
            value = !title.value.isNullOrBlank() && !content.value.isNullOrEmpty()
        }

        addSource(title) { merge() }
        addSource(content) { merge() }
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
        if (isEditable != this.isEditable) {
            _isEditable.value = isEditable
        }
    }

    fun addImageUri(uri: Uri) {
        val uris = mutableListOf<Uri>()
        imageUris.value?.filter { it != uri }?.let { uris.addAll(it) }
        uris.add(uri)

        _imageUris.value = uris
    }

    fun removeImageUri(uri: Uri) {
        val uris = mutableListOf<Uri>()
        imageUris.value?.let {
            it.toMutableList().also { it.remove(uri) }
        }?.also {
            uris.addAll(it)
        }
        _imageUris.value = uris
    }

    fun writeMemo() {
        if (title.value != null && content.value != null) {
            writeMemo(title.value!!, content.value!!, imageUris.value ?: mutableListOf())
        }
    }

    private fun writeMemo(title: String, content: String, uris: List<Uri>?) {
        val writeComplete = {
            setIsEditable(false)
        }
        uris?.let { setSavedImages(it) }
        disposable += when (memoUid) {
            Long.NONE -> memoRepository.insertMemo(title, content, uris)
                .compose(singleStateTransformer())
                .subscribe({
                    memoUid = it
                    writeComplete()
                }, { Log.e(this::class.java.simpleName, it.toString()) })

            else -> memoRepository.updateMemo(memoUid, title, content, uris)
                .compose(completableStateTransformer())
                .subscribe({
                    writeComplete()
                }, { Log.e(this::class.java.simpleName, it.toString()) })
        }
    }

    fun deleteMemo(onComplete: () -> Unit) {
        disposable += memoRepository.deleteMemo(memoUid)
            .compose(completableStateTransformer())
            .subscribe({
                onComplete()
            }, { Log.e(this::class.java.simpleName, it.toString()) })
    }

    private fun fetchMemo(uid: Long) {
        disposable += memoRepository.fetchMemo(uid)
            .compose(singleStateTransformer())
            .subscribe({
                title.value = it.title
                content.value = it.content
                it.pictures?.map { Uri.parse(it) }?.let { uris ->
                    _imageUris.value = uris
                    setSavedImages(uris)
                }
            }, { Log.e(this::class.java.simpleName, it.toString()) })
    }

    private fun setSavedImages(uris: List<Uri>) {
        savedImageUris.clear()
        savedImageUris.addAll(uris)
    }
}