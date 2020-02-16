package com.chazo826.memo.list.di

import com.chazo826.memo.list.ui.MemoListFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class MemoListFragmentProvider {

    @ContributesAndroidInjector(modules = [MemoListFragmentModule::class])
    abstract fun bindMemoListFragment(): MemoListFragment
}