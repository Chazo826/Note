package com.chazo826.core.dagger

import android.content.Intent
import android.provider.MediaStore
import androidx.fragment.app.FragmentActivity
import java.io.File

fun FragmentActivity.newIntentForImageAlbum(): Intent =
    Intent(Intent.ACTION_GET_CONTENT)
        .setType("image/*")

fun FragmentActivity.newIntentForCameraImage(): Intent =
    Intent(MediaStore.ACTION_IMAGE_CAPTURE).also {
        it.resolveActivity(packageManager)
    }
