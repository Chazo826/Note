package com.chazo826.memo.main

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.chazo826.core.dagger.android.DaggerAppCompatActivity
import com.chazo826.core.extensions.TAG
import com.chazo826.memo.R
import kotlinx.android.synthetic.main.activity_memo.*
import javax.inject.Inject

class MemoActivity : DaggerAppCompatActivity(R.layout.activity_memo) {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration

    private val viewModel: MemoMainViewModel by viewModels { viewModelFactory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        navController = findNavController(R.id.nav_host_fragment)
        appBarConfiguration = AppBarConfiguration(navController.graph)

        toolbar.setupWithNavController(navController, appBarConfiguration)
        setSupportActionBar(toolbar)

        setupLoading()
    }

    private fun setupLoading() {
        viewModel.loadingState.observe(this, Observer {
            Log.d(TAG, "isLoading: $it" )
            pg_loading?.isVisible = it
        })
        Log.d("ViewModel", "$viewModel")
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            android.R.id.home -> {
                navController.navigateUp()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}
