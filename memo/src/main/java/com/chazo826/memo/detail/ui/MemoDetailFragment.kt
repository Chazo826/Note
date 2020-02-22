package com.chazo826.memo.detail.ui

import android.Manifest
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Rect
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.*
import android.widget.EditText
import android.widget.Toast
import androidx.core.content.FileProvider
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.chazo826.core.constants.RequestCodeConsts
import com.chazo826.core.constants.RequestCodeConsts.PERMISSION_CAMERA
import com.chazo826.core.dagger.android.DaggerFragment
import com.chazo826.core.dagger.viewmodel_factory.CommonViewModelFactory
import com.chazo826.core.extensions.checkPermissionBeforeAction
import com.chazo826.core.extensions.isNotNone
import com.chazo826.core.extensions.isOK
import com.chazo826.core.extensions.showToast
import com.chazo826.core.newIntentForCameraImage
import com.chazo826.core.newIntentForImageAlbum
import com.chazo826.core.utils.createImageFile
import com.chazo826.memo.R
import com.chazo826.memo.databinding.FragmentMemoDetailBinding
import com.chazo826.memo.detail.adapter.AttachImageAdapter
import com.chazo826.memo.detail.viewmodel.MemoDetailViewModel
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

    private var imagesAdapter: AttachImageAdapter? = null

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
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        setupMenu()
        setupLoading()
        setupEditable()
        setupImages()
    }

    private fun setupMenu() {
        viewModel.menuInvalidate.observe(viewLifecycleOwner, Observer {
            activity?.invalidateOptionsMenu()
            imagesAdapter?.setIsEditable(viewModel.isEditable)
        })
    }

    private fun setupImages() {
        imagesAdapter =  AttachImageAdapter(disposable) {
            viewModel.removeImageUri(it)
        }

        binding.ryPictures.apply {
            adapter = imagesAdapter
            addItemDecoration(object : RecyclerView.ItemDecoration() {
                override fun getItemOffsets(
                    outRect: Rect,
                    view: View,
                    parent: RecyclerView,
                    state: RecyclerView.State
                ) {
                    super.getItemOffsets(outRect, view, parent, state)
                    val position = parent.getChildAdapterPosition(view)
                    val itemCount = state.itemCount

                    if(position < itemCount) {
                        outRect.right = resources.getDimension(R.dimen.memo_images_right_margin).toInt()
                    }
                }
            })
        }

        viewModel.imageUris.observe(viewLifecycleOwner, Observer {
            imagesAdapter?.submitList(it)
        })
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
                viewModel.deleteMemo {
                    findNavController().navigateUp()
                }
                true
            }
            R.id.action_save -> {
                binding.etContent.clearFocus()
                binding.etTitle.clearFocus()
                viewModel.writeMemo()
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
                    0 -> moveAlbumForImage()
                    1 -> moveCameraForImage()
                    2 -> moveURLInput()
                }
            }.show()
    }

    private fun moveAlbumForImage() {
        val writeExternalStoragePermission = Manifest.permission.WRITE_EXTERNAL_STORAGE

        checkPermissionBeforeAction(writeExternalStoragePermission, PERMISSION_CAMERA, R.string.camera_permission_rationale) {
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

        checkPermissionBeforeAction(writeExternalStoragePermission, PERMISSION_CAMERA, R.string.camera_permission_rationale) {
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

    private fun moveURLInput() {
        AlertDialog.Builder(context)
            .also {
                val editText = EditText(context)
                editText.hint = getString(R.string.memo_image_uri_input_hint)

                it.setView(editText)
                    .setPositiveButton(R.string.complete) { _, _ ->
                        addImageToContent(Uri.parse(editText.text.toString()))
                    }
            }.show()

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            RequestCodeConsts.IMAGE_ALBUM -> if (resultCode.isOK()) {
                data?.data?.let(::addImageToContent)
            }

            RequestCodeConsts.IMAGE_CAMERA -> if (resultCode.isOK()) {
                viewModel.photoUri?.let(::addImageToContent)
            }
        }
    }

    private fun addImageToContent(uri: Uri) {
        viewModel.addImageUri(uri)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            PERMISSION_CAMERA -> {
                if (grantResults.firstOrNull() == PackageManager.PERMISSION_DENIED) {
                    showToast(R.string.camera_permission_denied_guide)
                }
            }
            else -> super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }

    override fun onDestroyView() {
        disposable.clear()
        super.onDestroyView()
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