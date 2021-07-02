package com.skillbox.strava.ui.activity.main

import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.lifecycle.*
import androidx.navigation.NavController
import com.skillbox.core.extensions.*
import com.skillbox.core.extensions.setupBottomWithNavController
import com.skillbox.core.notification.NotificationChannels
import com.skillbox.core.platform.BaseActivity
import com.skillbox.core.snackbar.CustomSnackbar
import com.skillbox.core.state.StateCache
import com.skillbox.core.state.StateToolbar
import com.skillbox.shared_model.ToastModel
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
        screenViewModel.showNotificationObserver.observe(this, {
            showNotification()
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
        screenViewModel.showNotificationObserver.removeObserver {  }
        screenViewModel.reAuthStateObserver.removeObserver {  }
        super.onDestroy()
    }

    override fun localData(localToast: ToastModel) {
        if(localToast.text.isEmpty()) return
        CustomSnackbar.make(
                window.decorView.rootView as ViewGroup,
                localToast.isLocal,
                localToast.text,
                localToast.isError
        ) {
            screenViewModel.exit()
        }.show()
    }

    private fun showNotification() {
        val isEnable = NotificationChannels.isNotificationsEnabled(
                context = this,
                notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        )
        if(isEnable) {
            val notification =
                    NotificationChannels.buildNotificationEvent(this, getString(R.string.title_notification), getString(R.string.description_notification), R.drawable.ic_launcher_foreground)
            val notificationManager =
                    getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.notify(NotificationChannels.EVENT_ID, notification)
        } else {
            Log.d("CheckRunner", "Нотификации отключены пользователем")
        }
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