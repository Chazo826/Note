package com.chazo826.memo.memo

import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.chazo826.core.dagger.android.DaggerAppCompatActivity
import com.chazo826.memo.R
import kotlinx.android.synthetic.main.activity_memo.*

class MemoActivity : DaggerAppCompatActivity(R.layout.activity_memo) {
    private val navController: NavController by lazy { findNavController(R.id.memo_nav_graph) }
    private val appBarConfiguration: AppBarConfiguration by lazy { AppBarConfiguration(navController.graph) }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        NavigationUI.setupWithNavController(toolbar, navController, appBarConfiguration)
    }

}
