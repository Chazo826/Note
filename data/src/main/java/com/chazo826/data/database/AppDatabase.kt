package com.chazo826.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.chazo826.data.converter.AppDatabaseConverter
import com.chazo826.data.memo.cache.MemoDao
import com.chazo826.data.memo.model.Memo

@Database(entities = [Memo::class], version = 1)
@TypeConverters(AppDatabaseConverter::class)
abstract class AppDatabase: RoomDatabase() {
    abstract fun memoDao(): MemoDao
}