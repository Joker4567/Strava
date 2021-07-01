package com.skillbox.strava.ui.fragment.profile

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.app.ActivityCompat.finishAffinity
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.skillbox.core.platform.ViewBindingFragment
import com.skillbox.core.snackbar.CustomSnackbar
import com.skillbox.core.state.StateToolbar
import com.skillbox.shared_model.ToolbarModel
import com.skillbox.shared_model.network.Athlete
import com.skillbox.strava.R
import com.skillbox.strava.databinding.FragmentProfileBinding
import com.skillbox.strava.ui.activity.OnBoardingActivity
import com.skillbox.strava.ui.fragment.logOut.LogOutDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.item_contact.*

@AndroidEntryPoint
class ProfileFragment : ViewBindingFragment<FragmentProfileBinding>(FragmentProfileBinding::inflate) {

    override val screenViewModel by viewModels<ProfileViewModel>()
    private var userId: Long = 0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        screenViewModel.athleteObserver.observe(this, { athlete ->
            athlete?.let { setData(athlete) }
        })
        screenViewModel.getAthlete()
        screenViewModel.reAuthStateObserver.observe(viewLifecycleOwner, { isSuccessReAuth ->
            isSuccessReAuth?.let {
                if(isSuccessReAuth)
                {
                    startActivity(Intent(requireActivity(), OnBoardingActivity::class.java))
                    finishAffinity(requireActivity())
                }
            }
        })
        binding.profileButtonLogout.setOnClickListener {
            LogOutDialogFragment().show(requireActivity().supportFragmentManager, "DialogFragment")
        }
        binding.profileButtonShare.setOnClickListener {
            val action = ProfileFragmentDirections.actionHomeFragmentToContactFragment(userId)
            findNavController()
                    .navigate(action)
        }
        screenViewModel.toastObserver.observe(viewLifecycleOwner, { toastModel ->
            toastModel?.let {
                CustomSnackbar.make(
                        requireActivity().window.decorView.rootView as ViewGroup,
                        toastModel.isLocal,
                        toastModel.text
                ).show()
            }
        })
    }

    override fun onStart() {
        super.onStart()
        StateToolbar.changeToolbarTitle(ToolbarModel("Profile"))
    }

    private fun setData(model: Athlete) {
        userId = model.id
        model.profile?.let {
            Glide.with(requireContext())
                    .load(model.profile)
                    .placeholder(R.drawable.ic_placeholder_contact)
                    .error(R.drawable.ic_error_contact)
                    .transform(CircleCrop())
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