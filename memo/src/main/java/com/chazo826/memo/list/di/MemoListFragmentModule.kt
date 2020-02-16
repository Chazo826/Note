package com.chazo826.memo.list.di

import androidx.lifecycle.ViewModel
import com.chazo826.core.dagger.scope.ViewModelKey
import com.chazo826.memo.list.viewmodel.MemoListViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class MemoListFragmentModule {

    @Binds
    @IntoMap
    @ViewModelKey(MemoListViewModel::class)
    abstract fun bindMemoListViewModel(viewModel: MemoListViewModel): ViewModel
}