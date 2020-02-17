package com.chazo826.data.memo.cache

import androidx.paging.DataSource
import androidx.paging.PositionalDataSource
import androidx.room.*
import com.chazo826.data.memo.model.Memo

@Dao
interface MemoDao {

    @Query("SELECT * FROM memo order BY date DESC")
    fun fetchMemosByDate(): PositionalDataSource<Memo>

    @Query("SELECT * FROM memo WHERE uid == :uid")
    fun fetchMemo(uid: Long): Memo

    @Insert
    fun insert(memo: Memo)

    @Update
    fun update(memo: Memo)

    @Delete
    fun delete(memo: Memo)
}