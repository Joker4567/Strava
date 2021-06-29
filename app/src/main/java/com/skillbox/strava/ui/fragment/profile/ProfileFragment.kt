package com.skillbox.strava.ui.fragment.profile

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.viewModels
import com.skillbox.core.platform.ViewBindingFragment
import com.skillbox.core.state.StateTitleToolbar
import com.skillbox.shared_model.Athlete
import com.skillbox.strava.databinding.FragmentProfileBinding
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
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
        setSpinner(model.weight ?: 0)
    }

    private fun setSpinner(currentWeight: Int) {
        var weightData = arrayOf("29 kg").toMutableSet()
        (30..120).forEach { weight ->
            weightData.add("$weight kg")
        }
        val adapter: ArrayAdapter<String> = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, weightData.toList())
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        binding.profileSpinnerItems.setAdapter(adapter)

        binding.profileSpinnerItems?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val weightSelected = weightData.toList()[position].split(' ')[0].toInt()
                screenViewModel.changeWeight(weightSelected)
            }

        }
        if(currentWeight != 0){
            val index = weightData.toList().indexOfFirst { x -> x.contains(currentWeight.toString()) }
            binding.profileSpinnerItems.setSelection(index)
        }
    }
}