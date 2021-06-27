package com.skillbox.strava.ui.redirect

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import com.skillbox.core.platform.BaseActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RedirectActivity : BaseActivity() {

    override val screenViewModel by viewModels<RedirectViewModel>()

    override fun initInterface(savedInstanceState: Bundle?) {
        checkIntent(intent)
        screenViewModel.authStateObserver.observe(this, { result ->
            result?.let {
                if(it){
                    Log.d("", "")
                } else {
                    Log.d("", "")
                }
            }
        })
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
           }
        }
    }

    override fun onDestroy() {
        screenViewModel.authStateObserver.removeObserver { }
        super.onDestroy()
    }
}