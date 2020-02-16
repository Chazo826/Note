package com.chazo826.data.memo.cache

import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.chazo826.data.memo.model.Memo

@Dao
interface MemoDao {

    @Query("SELECT * FROM memo order BY date DESC")
    fun fetchMemoByDate(): DataSource<Int, Memo>

    @Insert
    fun insert(memo: Memo)

    @Delete
    fun delete(memo: Memo)
}