package com.chazo826.data.memo

import androidx.paging.PositionalDataSource
import com.chazo826.data.memo.model.Memo
import io.reactivex.Completable
import io.reactivex.Single

interface MemoRepository {

    fun fetchMemoPaginationByDate(): PositionalDataSource<Memo>

    fun fetchMemo(uid: Long): Single<Memo>

    fun insertMemo(title: String, content: String): Completable

    fun updateMemo(uid: Long, title: String, content: String): Completable

    fun deleteMemo(uid: Long): Completable
}