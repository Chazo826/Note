package com.chazo826.core

import android.content.Intent
import android.provider.MediaStore
import androidx.fragment.app.FragmentActivity

fun FragmentActivity.newIntentForImageAlbum(): Intent =
    Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
        type = "image/*"
        addCategory(Intent.CATEGORY_OPENABLE)
    }

fun FragmentActivity.newIntentForCameraImage(): Intent =
    Intent(MediaStore.ACTION_IMAGE_CAPTURE).also {
        it.resolveActivity(packageManager)
    }
