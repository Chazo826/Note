package com.chazo826.core.dagger.extensions

val Long.Companion.NONE: Long
    get() = -1L


fun Long.isNotNone() = this != Long.NONE
fun Long.isNone() = this == Long.NONE