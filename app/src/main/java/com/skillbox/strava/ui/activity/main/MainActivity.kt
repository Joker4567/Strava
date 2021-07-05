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
        // Выглядит как будто в метод понапихано все подряд
        if(savedInstanceState == null)
            setupBottomNavigationBar()


        ivExit.setOnClickListener {
            screenViewModel.exit()
        }

        // Вот эту настройку я бы в отдельную функцию вынес, настройку этого тулбара
        // Тулбар лучше использовать для каждого фрагмента отдельно, чем настраивать его в активити
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

        // Я бы посмотрел в сторону MVI, либо обсервы вынес в отдельную функцию, будет гораздо читаемее
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

        // Вот это вообще непонятно зачем
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
        // Ты передаешь lifecycle owner в подписку, по идее удалять подписчиков не нужно
        screenViewModel.showNotificationObserver.removeObserver {  }
        screenViewModel.reAuthStateObserver.removeObserver {  }
        super.onDestroy()
    }

    override fun localData(localToast: ToastModel) {
        if(localToast.text.isEmpty()) return // Лучше isBlank использовать иначе пройдет вариант с " "
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
                    NotificationChannels.buildNotificationEvent(this, getString(R.string.title_notification), getString(R.string.description_notification))
            val notificationManager =
                    getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.notify(NotificationChannels.EVENT_ID, notification)
        } else {
            // Ошибки лучше обрабатывать. а не просто логгировать
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