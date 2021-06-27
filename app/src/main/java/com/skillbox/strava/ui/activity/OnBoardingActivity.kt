package com.skillbox.strava.ui.activity

import android.os.Bundle
import androidx.lifecycle.LiveData
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.skillbox.core.extensions.setupBottomWithNavController
import com.skillbox.core.platform.BaseActivity
import com.skillbox.strava.R
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_app.*

@AndroidEntryPoint
class OnBoardingActivity : BaseActivity(R.layout.activity_app) {

    private var currentNavController: LiveData<NavController>? = null

    override fun initInterface(savedInstanceState: Bundle?) {
        if(savedInstanceState == null)
            setupBottomNavigationBar()
        intent?.data?.let { data ->
            val auth: Boolean = data.getQueryParameter("auth") as Boolean ?: false
            if(auth)
            {
                findNavController(R.id.fragmentContainer)
                        .navigate(R.id.action_boardingFragment_to_authFragment)
            }
        }
    }

    private fun setupBottomNavigationBar() {
        val navGraphIds = listOf(
                R.navigation.onboarding
        )

        val controller = bottom_nav.setupBottomWithNavController(
                navGraphIds = navGraphIds,
                fragmentManager = supportFragmentManager,
                containerId = R.id.fragmentContainer,
                intent = intent
        )
        currentNavController = controller
    }
}