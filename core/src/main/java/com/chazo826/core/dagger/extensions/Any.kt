package com.chazo826.core.dagger.extensions

val Any.TAG: String
    get() = this::class.java.simpleName