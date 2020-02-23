package com.chazo826.memo.main

import com.chazo826.core.viewmodel.StateBaseViewModel
import javax.inject.Inject

class MemoMainViewModel @Inject constructor(): StateBaseViewModel() {

    fun setLoadingStateValue(isLoading: Boolean) {
        _loadingState.postValue(isLoading)
    }
}