package com.chazo826.data.memo.cache

import androidx.paging.PositionalDataSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.chazo826.data.memo.model.Memo
import io.reactivex.Completable
import io.reactivex.Single

@Dao
interface MemoDao {

    @Query("SELECT * FROM memo order BY updatedAt DESC")
    fun fetchMemosByDate(): PositionalDataSource<Memo>

    @Query("SELECT * FROM memo WHERE uid == :uid")
    fun fetchMemo(uid: Long): Single<Memo>

    @Insert
    fun insert(memo: Memo): Single<Long>

    @Update
    fun update(memo: Memo): Completable

    @Query("DELETE FROM memo WHERE uid == :uid")
    fun delete(uid: Long): Completable
}