package com.chazo826.data.memo

import android.net.Uri
import androidx.paging.DataSource
import com.chazo826.data.memo.model.Memo
import io.reactivex.Completable
import io.reactivex.Single

interface MemoRepository {

    fun fetchMemoPaginationByDate(): DataSource.Factory<Int, Memo>

    fun fetchMemo(uid: Long): Single<Memo>

    fun insertMemo(title: String, content: String, uri: List<Uri>? = null): Single<Long>

    fun updateMemo(uid: Long, title: String, content: String, uri: List<Uri>? = null): Completable

    fun deleteMemo(uid: Long): Completable
}