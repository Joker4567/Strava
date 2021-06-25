package com.skillbox.strava.ui

import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.skillbox.core.platform.BaseActivity
import com.skillbox.strava.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AppActivity : BaseActivity(R.layout.activity_app) {

    private var _navController: NavController? = null
    private val navController get() = checkNotNull(_navController) { "navController isn`n initialized" }

    override fun initInterface(savedInstanceState: Bundle?) {
        _navController = Navigation.findNavController(this, R.id.fragmentContainer)
        NavigationUI.setupActionBarWithNavController(this, navController)
    }

    override fun onSupportNavigateUp(): Boolean {
        return NavigationUI.navigateUp(navController, null)
    }
}