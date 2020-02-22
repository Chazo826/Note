package com.chazo826.core.extensions

import android.content.pm.PackageManager
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment

fun Fragment.isPermissionGranted(permission: String): Boolean =
    ContextCompat.checkSelfPermission(requireContext(), permission) == PackageManager.PERMISSION_GRANTED

fun Fragment.showPermissionRationale(@StringRes stringId: Int) {
    showToast(stringId)
}

fun Fragment.checkPermissionBeforeAction(permission: String, requestCode: Int, @StringRes rationaleStringId: Int, action: () -> Unit) {
    if (!isPermissionGranted(permission)) {
        if (shouldShowRequestPermissionRationale(permission)) {
            showPermissionRationale(rationaleStringId)
        }
        requestPermissions(arrayOf(permission), requestCode)
    } else {
        action()
    }
}