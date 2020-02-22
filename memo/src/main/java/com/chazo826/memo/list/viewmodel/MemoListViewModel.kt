package com.chazo826.memo.list.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.chazo826.data.memo.MemoRepository
import com.chazo826.data.memo.model.Memo
import javax.inject.Inject

class MemoListViewModel @Inject constructor(
    private val memoRepository: MemoRepository
) : ViewModel() {

    val memos: LiveData<PagedList<Memo>> = fetchMemos()

    private fun fetchMemos(): LiveData<PagedList<Memo>> {
        val config = PagedList.Config.Builder()
            .setInitialLoadSizeHint(10)
            .setPageSize(10)
            .setPrefetchDistance(10)
            .setEnablePlaceholders(true)
            .build()

        return LivePagedListBuilder(memoRepository.fetchMemoPaginationByDate(), config).build()
    }
}