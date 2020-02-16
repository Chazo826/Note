package com.chazo826.note.dagger

import com.chazo826.memo.dagger.MemoActivityBuilder
import dagger.Module

@Module(includes = [MemoActivityBuilder::class])
class ActivityBuilders