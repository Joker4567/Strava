package com.skillbox.strava.ui.activity.redirect

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import com.skillbox.core.extensions.show
import com.skillbox.core.platform.BaseActivity
import com.skillbox.strava.R
import com.skillbox.strava.ui.activity.main.MainActivity
import com.skillbox.strava.ui.activity.OnBoardingActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_redirect.* // Синтетика устарела, нужно переходить либо на viewBinding либо findViewById

@AndroidEntryPoint
class RedirectActivity : BaseActivity(R.layout.activity_redirect) {

    override val screenViewModel by viewModels<RedirectViewModel>()

    override fun initInterface(savedInstanceState: Bundle?) {
        checkIntent(intent)
        screenViewModel.authStateObserver.observe(this, { result ->
            result?.let {
                if(it){
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finishAffinity()
                } else {
                    auth()
                }
            }
        })

        redirect_ivLogin?.setOnClickListener {
            auth()
        }
    }

    private fun auth() {
        val intent = Intent(this, OnBoardingActivity::class.java)
        intent.putExtra(AUTH, true)
        // Ключи нужно хранить в константах,
        // а то опечатка может быть очень критичной
        startActivity(intent)
        finishAffinity()
    }

    private fun checkIntent(intent: Intent?) {
        // Переписал кусок кода, сделал более котлиновским стиль
        intent?.action?.let { action ->
            if (action == Intent.ACTION_VIEW) {
                intent.data?.getQueryParameter(CODE)?.let {
                    screenViewModel.auth(it)
                }

                intent.data?.getQueryParameter(USER_ID)?.let { userId ->
                    redirect_webView?.show()
                    redirect_toolbar?.show()
                    redirect_webView?.loadUrl("https://www.strava.com/athletes/$userId")
                }
            }
        }
    }

    override fun onDestroy() {
        screenViewModel.authStateObserver.removeObserver { }
        super.onDestroy()
    }

    // Добавил константы, чтоб показать как это лучше сделать
    companion object {
        const val USER_ID = "userId"
        const val CODE = "code"
        const val AUTH = "auth"
    }
}