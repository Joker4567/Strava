package com.skillbox.strava.ui.activity.main

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.lifecycle.*
import androidx.navigation.NavController
import com.skillbox.core.extensions.*
import com.skillbox.core.extensions.setupBottomWithNavController
import com.skillbox.core.platform.BaseActivity
import com.skillbox.core.state.StateCache
import com.skillbox.core.state.StateToolbar
import com.skillbox.strava.R
import com.skillbox.strava.ui.activity.OnBoardingActivity
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
        StateToolbar.modelToolbar
                .flowWithLifecycle(lifecycle, Lifecycle.State.CREATED)
                .onEach { toolbarModel ->
                    redirect_toolbar.title = toolbarModel.title

                    if(toolbarModel.visible)
                        redirect_toolbar.show()
                    else
                        redirect_toolbar.gone()

                    if(toolbarModel.visibleLogOut)
                        ivExit.show()
                    else
                        ivExit.gone()
                }
                .catch {
                    Log.e("MainActivity", "StateTitleToolbar.titleToolbar -> ${it.localizedMessage}")
                }
                .launchIn(lifecycleScope)
        screenViewModel.reAuthStateObserver.observe(this, { isSuccessReAuth ->
            isSuccessReAuth?.let {
                if(isSuccessReAuth) {
                    exitProfile()
                }
            }
        })
        StateCache.isClearCache
                .flowWithLifecycle(lifecycle, Lifecycle.State.CREATED)
                .onEach { isClear ->
                    if(isClear){
                        screenViewModel.exit()
                    }
                }
                .catch {
                    Log.e("MainActivity", "StateCache.isClearCache -> ${it.localizedMessage}")
                }
                .launchIn(lifecycleScope)
    }

    override fun onDestroy() {
        screenViewModel.reAuthStateObserver.removeObserver {  }
        super.onDestroy()
    }

    private fun exitProfile() {
        startActivity(Intent(this, OnBoardingActivity::class.java))
        finishAffinity()
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