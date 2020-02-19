package com.chazo826.memo.detail.di

import androidx.lifecycle.ViewModel
import com.chazo826.core.dagger.extensions.NONE
import com.chazo826.core.dagger.scope.ViewModelKey
import com.chazo826.memo.detail.ui.MemoDetailFragment
import com.chazo826.memo.detail.viewmodel.MemoDetailViewModel
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import javax.inject.Named

@Module
abstract class MemoDetailFragmentModule {

    @Binds
    @IntoMap
    @ViewModelKey(MemoDetailViewModel::class)
    abstract fun bindMemoDetailViewModel(viewModel: MemoDetailViewModel): ViewModel

    @Module
    companion object {
        @Provides
        @JvmStatic
        @Named("memo_uid")
        fun provideMemoUid(fragment: MemoDetailFragment): Long {
            return fragment.arguments?.getLong(MemoDetailFragment.EXTRA_MEMO_UID) ?: Long.NONE
        }
    }
}