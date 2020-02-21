package com.chazo826.memo.list.ui

import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.chazo826.core.dagger.android.DaggerFragment
import com.chazo826.memo.R
import com.chazo826.memo.databinding.FragmentMemoListBinding


class MemoListFragment : DaggerFragment() {

    private lateinit var binding: FragmentMemoListBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_memo_list, container, false)
        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.memo_list, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            R.id.action_add -> {
                findNavController().navigate(R.id.addMemo)
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }
}