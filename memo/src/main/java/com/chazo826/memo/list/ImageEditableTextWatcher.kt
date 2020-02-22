package com.chazo826.memo.list

import android.text.Editable
import android.text.TextWatcher
import android.text.style.ImageSpan
import android.util.Log
import android.widget.EditText
import com.chazo826.core.dagger.extensions.TAG

class ImageEditableTextWatcher(
    private val editText: EditText
): TextWatcher {
    private val imageSpans by lazy { mutableListOf<ImageSpan>() }

    override fun afterTextChanged(s: Editable?) {
        Log.d(TAG, "afterTextChanged: ${s.toString()}")
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        Log.d(TAG, "beforeTextChanged: ${s.toString()}, start: $start, count: $count, after: $after")

    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        Log.d(TAG, "onTextChanged: ${s.toString()}, start: $start, before: $before, count: $count")
//        for()
    }
}