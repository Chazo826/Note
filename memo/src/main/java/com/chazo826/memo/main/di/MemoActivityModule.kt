package com.chazo826.memo.main.di

import androidx.lifecycle.ViewModel
import com.chazo826.core.dagger.scope.ActivityScope
import com.chazo826.core.dagger.scope.ViewModelKey
import com.chazo826.memo.main.MemoMainViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class MemoActivityModule {

    @Binds
    @IntoMap
    @ViewModelKey(MemoMainViewModel::class)
    @ActivityScope
    abstract fun bindMemoMainViewModel(viewModel: MemoMainViewModel): ViewModel
}