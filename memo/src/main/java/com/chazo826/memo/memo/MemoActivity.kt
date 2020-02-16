package com.chazo826.memo.memo

import android.os.Bundle
import com.chazo826.memo.R
import dagger.android.support.DaggerAppCompatActivity

class MemoActivity : DaggerAppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_memo)
    }

}
