package com.chazo826.memo.memo

import android.content.Intent
import androidx.fragment.app.FragmentActivity

object MemoActivityIntents {

    fun FragmentActivity.newIntentForMemoActivity(): Intent = Intent(this, MemoActivity::class.java)
}