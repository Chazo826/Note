package com.chazo826.note.dagger

import android.app.Application
import android.content.Context
import com.chazo826.data.dagger.DatabaseModule
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module(
    includes = [ViewModelFactoryModule::class,
        DatabaseModule::class]
)
class ApplicationModule {

    @Singleton
    @Provides
    fun provideContext(application: Application): Context {
        return application
    }
}