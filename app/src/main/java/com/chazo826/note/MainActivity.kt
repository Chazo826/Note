package com.chazo826.note

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.chazo826.memo.memo.MemoActivityIntents.newIntentForMemoActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)

        newIntentForMemoActivity().let(::startActivity)
        finish()
    }
}
