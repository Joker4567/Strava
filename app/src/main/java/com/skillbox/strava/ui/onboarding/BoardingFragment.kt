package com.skillbox.strava.ui.onboarding

import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.skillbox.strava.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BoardingFragment : Fragment(R.layout.fragment_boarding) {

    val screenViewModel by viewModels<BoardingViewModel>()

}