package com.chazo826.data.memo.cache

import androidx.paging.PositionalDataSource
import com.chazo826.data.memo.MemoRepository
import com.chazo826.data.memo.model.Memo
import io.reactivex.Single
import javax.inject.Inject

class MemoCacheDataSource @Inject constructor(
    private val memoDao: MemoDao
): MemoRepository {
    override fun fetchMemoPaginationByDate(): PositionalDataSource<Memo> {
        return memoDao.fetchMemosByDate()
    }

    override fun fetchMemo(uid: Long): Single<Memo> {
        return memoDao.fetchMemo(uid)
    }

    override fun insertMemo(title: String, content: String) {
        val updatedAt = System.currentTimeMillis()
        return memoDao.insert(Memo(title = title, content = content, updatedAt = updatedAt))
    }

    override fun updateMemo(uid: Long, title: String, content: String) {
        val updatedAt = System.currentTimeMillis()
        return memoDao.update(Memo(uid, title, updatedAt, content))
    }

    override fun deleteMemo(uid: Long) {
        return memoDao.delete(uid)
    }
}