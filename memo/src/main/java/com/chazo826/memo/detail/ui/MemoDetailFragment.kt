package com.chazo826.memo.detail.ui

import android.Manifest
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.text.Html
import android.text.Spanned
import android.text.style.ImageSpan
import android.view.*
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.chazo826.core.dagger.android.DaggerFragment
import com.chazo826.core.dagger.constants.RequestCodeConsts
import com.chazo826.core.dagger.constants.RequestCodeConsts.PERMISSION_CAMERA
import com.chazo826.core.dagger.extensions.*
import com.chazo826.core.dagger.newIntentForCameraImage
import com.chazo826.core.dagger.newIntentForImageAlbum
import com.chazo826.core.dagger.utils.createImageFile
import com.chazo826.core.dagger.viewmodel_factory.CommonViewModelFactory
import com.chazo826.memo.R
import com.chazo826.memo.databinding.FragmentMemoDetailBinding
import com.chazo826.memo.detail.viewmodel.MemoDetailViewModel
import com.chazo826.memo.list.ImageEditableTextWatcher
import com.jakewharton.rxbinding3.view.focusChanges
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import java.io.File
import java.io.IOException
import javax.inject.Inject

class MemoDetailFragment : DaggerFragment() {

    @Inject
    lateinit var viewModelFactory: CommonViewModelFactory

    private lateinit var binding: FragmentMemoDetailBinding

    private val viewModel: MemoDetailViewModel by viewModels { viewModelFactory }

    private val disposable by lazy { CompositeDisposable() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_memo_detail, container, false)
        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupMenu()
        setupLoading()
        setupTitle()
        setupContent()
        setupEditable()
    }

    private fun setupMenu() {
        viewModel.menuInvalidate.observe(viewLifecycleOwner, Observer {
            activity?.invalidateOptionsMenu()
        })
    }

    private fun setupTitle() {
        viewModel.title.observe(viewLifecycleOwner, Observer {
            binding.etTitle.setText(it)
        })
    }

    private fun setupContent() {
        viewModel.contentHtml.observe(viewLifecycleOwner, Observer {
            binding.etContent.setText(
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    Html.fromHtml(it, Html.FROM_HTML_MODE_LEGACY)
                } else {
                    @Suppress("DEPRECATION")
                    Html.fromHtml(it)
                }, TextView.BufferType.SPANNABLE
            )
        })

        binding.etContent.addTextChangedListener(ImageEditableTextWatcher(binding.etContent))

//        disposable += binding.etContent.textChanges()
//            .subscribe {
//                Log.d(this::class.java.simpleName, "EditText.ToString(): ${binding.etContent.text} ")
//
//                binding.etContent.text.getSpans<ImageSpan>(0, binding.etContent.length()).forEach {
//                    Log.d(this::class.java.simpleName, "ImageSpan.source : ${it.source} ")
//                    Log.d(this::class.java.simpleName, "ImageSpan.spanStart : ${binding.etContent.editableText.getSpanStart(it)} ")
//                }
//            }
    }


    private fun setupLoading() {
        viewModel.loadingState.observe(viewLifecycleOwner, Observer {
            binding.pgLoading.isVisible = it
        })
    }

    private fun setupEditable() {
        disposable += binding.etTitle.focusChanges().mergeWith(binding.etContent.focusChanges())
            .subscribe {
                if (it)
                    viewModel.setIsEditable(true)
            }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.memo_detail, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        val editButton = menu.findItem(R.id.action_edit)
        val saveButton = menu.findItem(R.id.action_save)
        val imageAddButton = menu.findItem(R.id.action_image)
        val deleteButton = menu.findItem(R.id.action_delete)

        editButton?.isVisible = !viewModel.isEditable
        imageAddButton?.isVisible = viewModel.isEditable
        deleteButton?.isVisible = viewModel.memoUid.isNotNone()

        saveButton?.let {
            it.isEnabled = viewModel.isDataExist.value == true
            it.isVisible = viewModel.isEditable
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_edit -> {
                binding.etTitle.requestFocus()
                viewModel.setIsEditable(true)
                true
            }
            R.id.action_delete -> {
                viewModel.deleteMemo()
                true
            }
            R.id.action_save -> {
                binding.etContent.clearFocus()
                binding.etTitle.clearFocus()
                viewModel.setIsEditable(false)
                true
            }
            R.id.action_image -> {
                showSelectDialogForImage()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun showSelectDialogForImage() {
        AlertDialog.Builder(context)
            .setItems(
                arrayOf(
                    getString(R.string.image_select_dialog_album),
                    getString(R.string.image_select_dialog_camera),
                    getString(R.string.image_select_dialog_url)
                )
            ) { _, which ->
                when (which) {
                    0 -> {
                        //TODO: 앨범
                        moveAlbumForImage()
                    }
                    1 -> {
                        //TODO: 카메라
                        moveCameraForImage()
                    }
                    2 -> {
                        //TODO: URL
                    }
                }
            }.show()
    }

    private fun moveAlbumForImage() {
        val writeExternalStoragePermission = Manifest.permission.WRITE_EXTERNAL_STORAGE

        checkPermissionBeforeAction(writeExternalStoragePermission) {
            activity?.newIntentForImageAlbum().also {
                startActivityForResult(it, RequestCodeConsts.IMAGE_ALBUM)
            }
        }
    }

    private fun moveCameraForImage() {
        if (activity?.packageManager?.hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY) == false) {
            showToast("카메라 기능을 지원하지 않는 디바이스입니다.", Toast.LENGTH_SHORT)
            return
        }

        val writeExternalStoragePermission = Manifest.permission.WRITE_EXTERNAL_STORAGE

        checkPermissionBeforeAction(writeExternalStoragePermission) {
            activity?.newIntentForCameraImage()?.also {
                val photoFile: File? = try {
                    activity?.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
                        ?.let(::createImageFile)
                } catch (ex: IOException) {
                    null
                }
                val photoUri = photoFile?.let { file ->
                    context?.let {
                        FileProvider.getUriForFile(
                            it,
                            "com.chazo826.note.fileprovider",
                            file
                        )
                    }
                }
                it.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
                viewModel.photoUri = photoUri
            }?.also {
                startActivityForResult(it, RequestCodeConsts.IMAGE_CAMERA)
            }
        }
    }

    private fun checkPermissionBeforeAction(permission: String, action: () -> Unit) {
        if (!isPermissionGranted(permission)) {
            if (shouldShowRequestPermissionRationale(permission)) {
                showPermissionRationale(R.string.app_name)
            }
            requestPermissions(arrayOf(permission), PERMISSION_CAMERA)
        } else {
            action()
        }
    }

    private fun moveURLInput() {

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            RequestCodeConsts.IMAGE_ALBUM -> if (resultCode.isOK()) {
                data?.data?.let(::addImageSpanToContent)
            }

            RequestCodeConsts.IMAGE_CAMERA -> if (resultCode.isOK()) {
                viewModel.photoUri?.let(::addImageSpanToContent)
            }
        }
    }

    private fun addImageSpanToContent(uri: Uri) {
        context?.let { context ->
            setImageSpanTarget(uri)?.let {
                Glide.with(context)
                    .asDrawable()
                    .load(uri)
                    .override(10, 10)
                    .placeholder(createCircleProgress())
                    .error(R.drawable.ic_error)
                    .into(it)
            }
        }
    }

    private fun setImageSpanTarget(uri: Uri): CustomTarget<Drawable>? {
        val editText = binding.etContent

        val message = editText.editableText
        val selectionStart = editText.selectionStart
        val text = "IMAGE "
        message.replace(selectionStart, editText.selectionEnd, text)

        return object : CustomTarget<Drawable>() {
            override fun onLoadCleared(placeholder: Drawable?) {
                context?.let { context ->
                    val imageSpan = ImageSpan(context, uri)
                    message.setSpan(
                        imageSpan,
                        selectionStart,
                        selectionStart + text.length,
                        Spanned.SPAN_INCLUSIVE_INCLUSIVE
                    )
                }
            }

            override fun onResourceReady(
                resource: Drawable,
                transition: Transition<in Drawable>?
            ) {
                context?.let { context ->
                    val imageSpan = ImageSpan(context, uri)
                    message.setSpan(
                        imageSpan,
                        selectionStart,
                        selectionStart + text.length,
                        Spanned.SPAN_INCLUSIVE_INCLUSIVE
                    )
                }
            }
        }
    }

    private fun createCircleProgress(): Drawable {
        val circularProgressDrawable = CircularProgressDrawable(requireContext())
        circularProgressDrawable.strokeWidth = 5f
        circularProgressDrawable.centerRadius = 30f
        circularProgressDrawable.start()
        return circularProgressDrawable
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            PERMISSION_CAMERA -> {
                if (grantResults.firstOrNull() == PackageManager.PERMISSION_DENIED) {
                    showToast("권한이 없으면 이미지 첨부기능을 사용하실 수 없습니다.")
                }
            }
            else -> super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }

    companion object {
        const val EXTRA_MEMO_UID = "extra_memo_uid"
        fun newBundle(uid: Long): Bundle {
            return Bundle().apply {
                putLong(EXTRA_MEMO_UID, uid)
            }
        }
    }
}