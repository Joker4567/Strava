package com.skillbox.strava.ui.fragment.profile

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.skillbox.core.platform.ViewBindingFragment
import com.skillbox.core.state.StateTitleToolbar
import com.skillbox.shared_model.Athlete
import com.skillbox.strava.R
import com.skillbox.strava.databinding.FragmentProfileBinding
import com.skillbox.strava.ui.fragment.onboarding.BoardingViewModel
import dagger.hilt.android.AndroidEntryPoint
import com.skillbox.strava.ui.activity.main.MainActivity

import com.squareup.picasso.Picasso
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation


@AndroidEntryPoint
class ProfileFragment : ViewBindingFragment<FragmentProfileBinding>(FragmentProfileBinding::inflate) {

    override val screenViewModel by viewModels<ProfileViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        screenViewModel.athleteObserver.observe(this, { athlete ->
            athlete?.let { setData(athlete) }
        })
        screenViewModel.getAthlete()
    }

    override fun onStart() {
        super.onStart()
        StateTitleToolbar.changeToolbarTitle("Profile")
    }

    private fun setData(model: Athlete) {
        model.profile?.let {
            val transformation = RoundedCornersTransformation(25, 0, RoundedCornersTransformation.CornerType.ALL)
            Picasso
                    .get()
                    .load(model.profile)
                    .transform(transformation)
                    .into(binding.profileImageViewPhoto)
        }
        binding.profileTvName.text = "${model.lastname} ${model.firstname}"
        binding.profileTvCountFollowers.text = "${model.follower ?: 0}"
        binding.profileTvCountFlollowing.text = "${model.friend ?: 0}"
        binding.profileTvGenderValue.text = "${model.sex?.name ?: "not sex"}"
        binding.profileTvCountryValue.text = "${model.country ?: "not country"}"
    }
}