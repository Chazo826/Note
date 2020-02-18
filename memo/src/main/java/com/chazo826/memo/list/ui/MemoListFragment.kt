package com.chazo826.memo.list.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
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
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_memo_list,
            container,
            false
        )
        return binding.root
    }
}