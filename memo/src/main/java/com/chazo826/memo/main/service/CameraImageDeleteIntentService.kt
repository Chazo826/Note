package com.chazo826.memo.main.service

import android.app.IntentService
import android.content.Intent
import android.net.Uri
import android.util.Log
import com.chazo826.core.extensions.TAG
import com.chazo826.memo.main.IntentConsts.EXTRA_URIS

class CameraImageDeleteIntentService : IntentService(CameraImageDeleteIntentService::class.java.simpleName) {
    override fun onHandleIntent(intent: Intent?) {
        intent?.getParcelableArrayListExtra<Uri>(EXTRA_URIS)?.filter {
            it.authority == "com.chazo826.note.fileprovider"
        }?.forEach {
            val deleteItemCount = baseContext.contentResolver.delete(it, null, null)
            Log.d(TAG, "camera capture image delete = $deleteItemCount")
        }
    }


}