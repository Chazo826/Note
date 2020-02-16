package com.chazo826.note.dagger

import androidx.lifecycle.ViewModelProvider
import com.chazo826.core.dagger.viewmodel_factory.CommonViewModelFactory
import dagger.Binds
import dagger.Module

@Module
abstract class ViewModelFactoryModule {
    @Binds
    abstract fun bindViewModelFactory(viewModelFactory: CommonViewModelFactory): ViewModelProvider.Factory
}