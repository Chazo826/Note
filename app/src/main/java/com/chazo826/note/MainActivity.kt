package com.chazo826.note

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.chazo826.memo.main.MemoIntents.newIntentForMemoActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        newIntentForMemoActivity().let(::startActivity)
        finish()
    }
}
