package com.chazo826.core.extensions

import android.content.pm.PackageManager
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment

fun Fragment.isPermissionGranted(permission: String): Boolean =
    ContextCompat.checkSelfPermission(requireContext(), permission) == PackageManager.PERMISSION_GRANTED

fun Fragment.isPermissionsGranted(permissions: Array<String>): Boolean =
    permissions.fold(initial = true) { before, value -> before && isPermissionGranted(value) }

fun Fragment.showPermissionRationale(@StringRes stringId: Int) {
    showToast(stringId)
}

fun Fragment.checkPermissionsBeforeAction(permissions: Array<String>, requestCode: Int, @StringRes rationaleStringId: Int, action: () -> Unit) {
    if (!isPermissionsGranted(permissions)) {
        permissions.forEach {
            if (shouldShowRequestPermissionRationale(it)) {
                showPermissionRationale(rationaleStringId)
            }
        }
        requestPermissions(permissions, requestCode)
    } else {
        action()
    }
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