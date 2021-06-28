package com.skillbox.strava.ui.activity.main

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LiveData
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import com.skillbox.core.extensions.setupBottomWithNavController
import com.skillbox.core.platform.BaseActivity
import com.skillbox.core.state.StateTitleToolbar
import com.skillbox.strava.R
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.flow.*

@AndroidEntryPoint
class MainActivity : BaseActivity(R.layout.activity_main) {

    override val screenViewModel by viewModels<MainViewModel>()
    private var currentNavController: LiveData<NavController>? = null

    override fun initInterface(savedInstanceState: Bundle?) {
        if(savedInstanceState == null)
            setupBottomNavigationBar()
        ivExit.setOnClickListener {
            screenViewModel.exit()
        }
        StateTitleToolbar.titleToolbar
                .flowWithLifecycle(lifecycle, Lifecycle.State.CREATED)
                .onEach { title ->
                    toolbar.title = title
                }
                .catch {
                    Log.e("MainActivity", "StateTitleToolbar.titleToolbar -> ${it.localizedMessage}")
                }
                .launchIn(lifecycleScope)
    }

    private fun setupBottomNavigationBar() {
        val navGraphIds = listOf(
                R.navigation.home,
                R.navigation.activities
        )

        val controller = bottom_nav.setupBottomWithNavController(
                navGraphIds = navGraphIds,
                fragmentManager = supportFragmentManager,
                containerId = R.id.nav_host_container,
                intent = intent
        )
        currentNavController = controller
    }
}