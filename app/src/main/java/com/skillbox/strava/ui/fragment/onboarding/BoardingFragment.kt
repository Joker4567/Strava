package com.skillbox.strava.ui.fragment.onboarding

import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.skillbox.core.extensions.checkDarkTheme
import com.skillbox.core.platform.ViewBindingFragment
import com.skillbox.core.utils.ZoomOutPageTransformer
import com.skillbox.core_db.pref.Pref
import com.skillbox.shared_model.network.BoardingModel
import com.skillbox.strava.R
import com.skillbox.strava.databinding.FragmentBoardingBinding
import com.skillbox.strava.ui.fragment.onboarding.adapter.OnboardingAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BoardingFragment : ViewBindingFragment<FragmentBoardingBinding>(FragmentBoardingBinding::inflate) {

    override val screenViewModel by viewModels<BoardingViewModel>()
    private var screens: MutableList<BoardingModel> = emptyList<BoardingModel>().toMutableList()
    private var positionSelected = 0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val imageDrawable = if (requireContext().checkDarkTheme())
            AppCompatResources.getDrawable(requireContext(), R.drawable.ic_logo_dark)
        else
            requireContext().getDrawable(R.drawable.ic_logo_light)

        binding.boardingImageLogo.setBackgroundDrawable(imageDrawable)
        bindViewPager()
    }

    private fun bindViewPager() {
        // Вот это все делается внутри вьюМодели, все что нужно для условий передается параметрами
        screens.clear()
        screens.addAll(
                listOf(
                        BoardingModel(
                                R.string.boarding_card_title_one,
                                R.string.boarding_card_desc_one,
                                R.drawable.ic_welcome_ascent
                        ),
                        BoardingModel(
                                R.string.boarding_card_title_two,
                                R.string.boarding_card_desc_two,
                                if(requireContext().checkDarkTheme()) R.drawable.ic_friends_dark else R.drawable.ic_friends
                        ),
                        BoardingModel(
                                R.string.boarding_card_title_tree,
                                R.string.boarding_card_desc_tree,
                                if(requireContext().checkDarkTheme()) R.drawable.ic_activities_dark else R.drawable.ic_activities
                        )
                )
        )

        val adapter = OnboardingAdapter(screens, requireActivity())
        binding.boardingCard.adapter = adapter
        binding.boardingCard.setPageTransformer(ZoomOutPageTransformer())
        binding.boardingCard.offscreenPageLimit = 1
        binding.boardingDotsIndicator.setViewPager2(binding.boardingCard)

        binding.boardingSkip.setOnClickListener {
            if (positionSelected < 2)
                binding.boardingCard.setCurrentItem(positionSelected + 1, true)
            else
                navigateAuth()
        }

        binding.boardingCard.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                positionSelected = position
                binding.boardingSkip.text = if (position < 2) getString(R.string.boarding_button_skip) else getString(R.string.boarding_button_okey)
            }
        })
    }

    private fun navigateAuth() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Pref(requireContext()).isBoarding = true
        }
        findNavController()
                .navigate(R.id.action_boardingFragment_to_authFragment)
    }
}