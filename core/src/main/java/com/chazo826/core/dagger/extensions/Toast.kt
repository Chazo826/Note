package com.chazo826.core.dagger.extensions

import android.widget.Toast
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity



fun Fragment.showToast(@StringRes stringResId: Int, showLength: Int = Toast.LENGTH_SHORT) {
    activity?.showToast(stringResId, showLength)
}

fun Fragment.showToast(text: String, showLength: Int = Toast.LENGTH_SHORT) {
    activity?.showToast(text, showLength)
}

fun FragmentActivity.showToast(@StringRes stringResId: Int, showLength: Int = Toast.LENGTH_SHORT) {
    showToast(getString(stringResId), showLength)
}

fun FragmentActivity.showToast(text: String, showLength: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, text, showLength).show()
}