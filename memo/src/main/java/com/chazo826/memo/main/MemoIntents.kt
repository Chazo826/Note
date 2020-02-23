package com.chazo826.memo.main

import android.content.Intent
import android.net.Uri
import androidx.fragment.app.FragmentActivity
import com.chazo826.memo.main.IntentConsts.EXTRA_URIS
import com.chazo826.memo.main.service.CameraImageDeleteIntentService

object MemoIntents {

    fun FragmentActivity.newIntentForMemoActivity(): Intent =
        Intent(this, MemoActivity::class.java)

    fun FragmentActivity.newIntentForCameraImageDeleteIntentService(uris: List<Uri>): Intent =
        Intent(this, CameraImageDeleteIntentService::class.java).apply {
            putParcelableArrayListExtra(EXTRA_URIS, ArrayList(uris))
        }
}

