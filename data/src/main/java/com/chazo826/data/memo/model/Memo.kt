package com.chazo826.data.memo.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Memo(
    @PrimaryKey(autoGenerate = true) val uid: Long,
    val title: String?,
    val date: Long?,
    val content: String?,
    val pictures: List<String>?
)