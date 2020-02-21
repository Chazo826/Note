package com.chazo826.core.dagger.utils

import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

@Throws(IOException::class)
fun createImageFile(fileDir: File): File {
    val timeStamp: String = SimpleDateFormat("yyyy_mm_dd_hh_mm", Locale.getDefault()).format(Date())

    return File.createTempFile("CAPTURE_${timeStamp}_",
        ".jpg",
        fileDir
    )
}