package com.chazo826.memo.main

import android.content.Intent
import androidx.fragment.app.FragmentActivity

object MemoIntents {

    fun FragmentActivity.newIntentForMemoActivity(): Intent =
        Intent(this, MemoActivity::class.java)
}