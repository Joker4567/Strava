package com.skillbox.strava.ui.fragment.activities

import com.skillbox.core.platform.ViewBindingFragment
import com.skillbox.core.state.StateTitleToolbar
import com.skillbox.strava.databinding.FragmentActivitiesBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ActivitiesFragment : ViewBindingFragment<FragmentActivitiesBinding>(FragmentActivitiesBinding::inflate) {
    override fun onStart() {
        super.onStart()
        StateTitleToolbar.changeToolbarTitle("Activities")
    }
}