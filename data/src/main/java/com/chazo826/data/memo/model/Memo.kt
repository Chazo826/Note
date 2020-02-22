package com.chazo826.data.memo.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Memo(
    @PrimaryKey(autoGenerate = true) val uid: Long = 0,
    val title: String?,
    val updatedAt: Long?,
    val content: String?,
    val pictures: List<String>? = null
)