package com.skillbox.strava.ui.fragment.activities

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.skillbox.core.platform.ViewBindingFragment
import com.skillbox.core.state.StateTitleToolbar
import com.skillbox.strava.R
import com.skillbox.strava.databinding.FragmentActivitiesBinding
import com.skillbox.strava.ui.fragment.onboarding.BoardingViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ActivitiesFragment : ViewBindingFragment<FragmentActivitiesBinding>(FragmentActivitiesBinding::inflate) {

    override val screenViewModel by viewModels<BoardingViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.floatingActionButton.setOnClickListener {
            findNavController()
                    .navigate(R.id.action_activitiesFragment_to_addActivitiesFragment)
        }
    }

    override fun onStart() {
        super.onStart()
        StateTitleToolbar.changeToolbarTitle("Activities")
    }
}