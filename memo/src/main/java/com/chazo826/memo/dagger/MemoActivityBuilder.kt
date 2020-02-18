package com.chazo826.memo.dagger

import com.chazo826.core.dagger.scope.ActivityScope
import com.chazo826.memo.detail.di.MemoDetailFragmentProvider
import com.chazo826.memo.list.di.MemoListFragmentProvider
import com.chazo826.memo.memo.MemoActivity
import com.chazo826.memo.memo.di.MemoActivityModule
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class MemoActivityBuilder {

    @ActivityScope
    @ContributesAndroidInjector(
        modules = [MemoActivityModule::class,
            MemoListFragmentProvider::class,
            MemoDetailFragmentProvider::class]
    )
    abstract fun bindMemoActivity(): MemoActivity
}