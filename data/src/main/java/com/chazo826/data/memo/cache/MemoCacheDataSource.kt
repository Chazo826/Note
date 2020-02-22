package com.chazo826.data.memo.cache

import android.net.Uri
import androidx.paging.DataSource
import com.chazo826.data.memo.MemoRepository
import com.chazo826.data.memo.model.Memo
import io.reactivex.Completable
import io.reactivex.Single
import javax.inject.Inject

class MemoCacheDataSource @Inject constructor(
    private val memoDao: MemoDao
): MemoRepository {
    override fun fetchMemoPaginationByDate(): DataSource.Factory<Int, Memo> {
        return memoDao.fetchMemosByDate()
    }

    override fun fetchMemo(uid: Long): Single<Memo> {
        return memoDao.fetchMemo(uid)
    }

    override fun insertMemo(title: String, content: String, uri: List<Uri>?): Single<Long> {
        val updatedAt = System.currentTimeMillis()
        return memoDao.insert(Memo(title = title, content = content, updatedAt = updatedAt, pictures = uri?.map { it.toString() }))
    }

    override fun updateMemo(uid: Long, title: String, content: String, uri: List<Uri>?): Completable {
        val updatedAt = System.currentTimeMillis()
        return memoDao.update(Memo(uid, title, updatedAt, content, uri?.map { it.toString() }))
    }

    override fun deleteMemo(uid: Long): Completable {
        return memoDao.delete(uid)
    }
}