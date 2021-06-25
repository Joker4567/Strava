package com.skillbox.strava.ui.redirect

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import com.skillbox.core.platform.BaseActivity
import com.skillbox.strava.R
import com.skillbox.strava.ui.onboarding.BoardingViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RedirectActivity : BaseActivity() {

    override val screenViewModel by viewModels<RedirectViewModel>()

    override fun initInterface(savedInstanceState: Bundle?) {
        checkIntent(intent)
    }

    private fun checkIntent(intent: Intent?) {
        val data = intent?.data
        val action = intent?.action
        if(data != null && action != null) {
           if(action == Intent.ACTION_VIEW)
           {
               data.getQueryParameter("code")
           }
        }
        Log.d("LGT_INTENT", "data -> $data, action -> $action")
    }
}