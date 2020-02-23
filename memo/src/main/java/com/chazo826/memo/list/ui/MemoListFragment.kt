package com.chazo826.memo.list.ui

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.chazo826.core.constants.RequestCodeConsts
import com.chazo826.core.dagger.android.DaggerFragment
import com.chazo826.core.extensions.TAG
import com.chazo826.core.extensions.checkPermissionBeforeAction
import com.chazo826.core.extensions.showToast
import com.chazo826.memo.R
import com.chazo826.memo.databinding.FragmentMemoListBinding
import com.chazo826.memo.detail.ui.MemoDetailFragment
import com.chazo826.memo.list.adapter.MemoPagedAdapter
import com.chazo826.memo.list.viewmodel.MemoListViewModel
import com.chazo826.memo.main.MemoMainViewModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import javax.inject.Inject


class MemoListFragment : DaggerFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var binding: FragmentMemoListBinding

    private val viewModel by viewModels<MemoListViewModel> { viewModelFactory }

    private val mainViewModel: MemoMainViewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory).get(MemoMainViewModel::class.java)
    }

    private var memosAdapter: MemoPagedAdapter? = null

    private val disposable by lazy { CompositeDisposable() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_memo_list, container, false)
        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupMemos()
        setupLoading()
    }

    private fun setupLoading() {
        viewModel.loadingState.observe(viewLifecycleOwner, Observer {
            mainViewModel.setLoadingStateValue(it)
        })
    }

    private fun setupMemos() {
        memosAdapter = MemoPagedAdapter(disposable) { memoUid ->
            findNavController().navigate(R.id.addMemo, MemoDetailFragment.newBundle(memoUid))
        }

        binding.ryMemos.apply {
            layoutManager = StaggeredGridLayoutManager(2, RecyclerView.VERTICAL)
            addItemDecoration(object : RecyclerView.ItemDecoration() {
                override fun getItemOffsets(
                    outRect: Rect,
                    view: View,
                    parent: RecyclerView,
                    state: RecyclerView.State
                ) {
                    super.getItemOffsets(outRect, view, parent, state)

                    outRect.right = resources.getDimension(R.dimen.memos_item_end_margin).toInt()
                    outRect.bottom = resources.getDimension(R.dimen.memos_item_bottom_margin).toInt()
                }
            })
            adapter = memosAdapter
        }

        val permission = Manifest.permission.WRITE_EXTERNAL_STORAGE

        checkPermissionBeforeAction(
            permission,
            RequestCodeConsts.PERMISSION_FOR_EXTERNAL_STORAGE,
            R.string.memos_image_permission_rationale
        ) {
            bindMemos()
        }
    }

    private fun bindMemos() {
        disposable += viewModel.memos.subscribe({
            memosAdapter?.submitList(it)
        }, { Log.e(TAG, it.toString())})
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.memo_list, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_add -> {
                findNavController().navigate(R.id.addMemo)
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            RequestCodeConsts.PERMISSION_FOR_EXTERNAL_STORAGE -> {
                if (grantResults.firstOrNull() == PackageManager.PERMISSION_DENIED) {
                    showToast(R.string.memos_image_permission_denied_guide)
                }
                bindMemos()
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onDestroyView() {
        disposable.clear()
        super.onDestroyView()
    }
}