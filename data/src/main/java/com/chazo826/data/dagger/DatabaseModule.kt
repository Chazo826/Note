package com.chazo826.data.dagger

import android.content.Context
import androidx.room.Room
import com.chazo826.data.database.AppDatabase
import dagger.Module
import dagger.Provides

@Module
class DatabaseModule {

    @Provides
    fun provideAppDatabase(context: Context): AppDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            AppDatabase::class.java,
            "chazo-database"
        ).build()
    }
}