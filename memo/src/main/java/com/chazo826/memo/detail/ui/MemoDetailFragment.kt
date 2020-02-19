package com.chazo826.memo.detail.ui

import android.os.Build
import android.os.Bundle
import android.text.Html
import android.view.*
import android.widget.TextView
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.chazo826.core.dagger.android.DaggerFragment
import com.chazo826.core.dagger.viewmodel_factory.CommonViewModelFactory
import com.chazo826.memo.R
import com.chazo826.memo.databinding.FragmentMemoDetailBinding
import com.chazo826.memo.detail.viewmodel.MemoDetailViewModel
import kotlinx.android.synthetic.main.activity_memo.*
import javax.inject.Inject

class MemoDetailFragment : DaggerFragment() {

    @Inject
    lateinit var viewModelFactory: CommonViewModelFactory

    private lateinit var binding: FragmentMemoDetailBinding

    private val viewModel: MemoDetailViewModel by viewModels { viewModelFactory }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMemoDetailBinding.inflate(inflater, container, false)
        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    private fun setupTitle() {
        viewModel.title.observe(viewLifecycleOwner, Observer {
            binding.etTitle.setText(it)
        })
    }

    private fun setupContent() {
        viewModel.contentHtml.observe(viewLifecycleOwner, Observer {
            binding.etContent.setText(if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                Html.fromHtml(it, Html.FROM_HTML_MODE_LEGACY)
            } else {
                Html.fromHtml(it)
            }, TextView.BufferType.SPANNABLE)
        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.memo_detail, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            R.id.action_edit -> {
                true
            }
            R.id.action_save -> {
                true
            }
            R.id.action_image -> {
                true
            }
            else -> super.onOptionsItemSelected(item)
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