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
import kotlinx.android.synthetic.main.activity_redirect.*

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
        intent.putExtra("auth", true)
        startActivity(intent)
        finishAffinity()
    }

    private fun checkIntent(intent: Intent?) {
        val data = intent?.data
        val action = intent?.action
        if(data != null && action != null) {
           if(action == Intent.ACTION_VIEW)
           {
               data.getQueryParameter("code")?.let { code ->
                   screenViewModel.auth(code)
               }
               //https://strava/athletes/?userId=87785387 -> https://www.strava.com/athletes/87785387
               data.getQueryParameter("userId")?.let { userId ->
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
}