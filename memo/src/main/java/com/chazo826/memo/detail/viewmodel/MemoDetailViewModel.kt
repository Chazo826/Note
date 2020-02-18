package com.chazo826.memo.detail.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import javax.inject.Inject

class MemoDetailViewModel @Inject constructor() : ViewModel() {

    private val _title = MutableLiveData<String>()
    val title: LiveData<String>
        get() = _title

    private val _contentHtml = MutableLiveData<String>()
    val contentHtml: LiveData<String>
        get() = _contentHtml
}