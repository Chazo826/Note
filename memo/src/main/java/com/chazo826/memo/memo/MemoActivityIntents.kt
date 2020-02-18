package com.chazo826.memo.memo

import android.content.Context
import android.content.Intent

object MemoActivityIntents {

    fun Context.newIntentForMemoActivity(): Intent = Intent(this, MemoActivity::class.java)
}