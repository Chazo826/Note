package com.chazo826.memo.detail.di

import androidx.lifecycle.ViewModel
import com.chazo826.core.dagger.scope.ViewModelKey
import com.chazo826.memo.detail.viewmodel.MemoDetailViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class MemoDetailFragmentModule {

    @Binds
    @IntoMap
    @ViewModelKey(MemoDetailViewModel::class)
    abstract fun bindMemoDetailViewModel(viewModel: MemoDetailViewModel): ViewModel
}