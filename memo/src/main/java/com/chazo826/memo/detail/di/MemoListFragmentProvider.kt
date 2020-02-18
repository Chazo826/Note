package com.chazo826.memo.detail.di

import com.chazo826.memo.detail.ui.MemoDetailFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class MemoDetailFragmentProvider {

    @ContributesAndroidInjector(modules = [MemoDetailFragmentModule::class])
    abstract fun bindMemoDetailFragment(): MemoDetailFragment
}