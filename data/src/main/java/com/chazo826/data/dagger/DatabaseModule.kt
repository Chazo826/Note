package com.chazo826.data.dagger

import android.content.Context
import androidx.room.Room
import com.chazo826.data.database.AppDatabase
import com.chazo826.data.memo.MemoRepository
import com.chazo826.data.memo.cache.MemoCacheDataSource
import com.chazo826.data.memo.cache.MemoDao
import dagger.Binds
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
abstract class DatabaseModule {

    @Module
    companion object {
        @Provides
        @Singleton
        @JvmStatic
        fun provideAppDatabase(context: Context): AppDatabase {
            return Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "chazo-database"
            ).build()
        }

        @Provides
        @Singleton
        @JvmStatic
        fun provideMemoDao(appDatabase: AppDatabase): MemoDao {
            return appDatabase.memoDao()
        }
    }


    @Binds
    abstract fun provideMemo(memoCacheDataSource: MemoCacheDataSource): MemoRepository
}