package com.chazo826.core.dagger.extensions

import android.content.Intent
import android.provider.MediaStore
import androidx.fragment.app.FragmentActivity

fun FragmentActivity.newIntentForImageAlbum(): Intent =
    intent.setAction(Intent.ACTION_GET_CONTENT)
        .setType("image/*")

fun FragmentActivity.newIntentForCameraImage(): Intent =
    Intent(MediaStore.ACTION_IMAGE_CAPTURE).also {
        it.resolveActivity(packageManager)
    }