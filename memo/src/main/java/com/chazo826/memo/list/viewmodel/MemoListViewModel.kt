package com.chazo826.memo.list.viewmodel

import androidx.paging.PagedList
import androidx.paging.RxPagedListBuilder
import com.chazo826.core.viewmodel.StateBaseViewModel
import com.chazo826.data.memo.MemoRepository
import com.chazo826.data.memo.model.Memo
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class MemoListViewModel @Inject constructor(
    private val memoRepository: MemoRepository
) : StateBaseViewModel() {
    val memos: Observable<PagedList<Memo>> = fetchMemos()

    private fun fetchMemos(): Observable<PagedList<Memo>> {
        val config = PagedList.Config.Builder()
            .setInitialLoadSizeHint(5)
            .setPageSize(5)
            .setPrefetchDistance(5)
            .setEnablePlaceholders(true)
            .build()

        return RxPagedListBuilder(memoRepository.fetchMemoPaginationByDate(), config)
            .setFetchScheduler(Schedulers.io())
            .setNotifyScheduler(AndroidSchedulers.mainThread())
            .buildObservable()
            .doOnSubscribe { _loadingState.postValue(true) }
            .doAfterNext { _loadingState.postValue(false) }
            .doFinally { _loadingState.postValue(false) }
    }
}