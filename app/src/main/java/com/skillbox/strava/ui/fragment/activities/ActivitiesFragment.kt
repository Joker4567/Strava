package com.skillbox.strava.ui.fragment.activities

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.hannesdorfmann.adapterdelegates4.ListDelegationAdapter
import com.skillbox.core.extensions.setData
import com.skillbox.core.extensions.*
import com.skillbox.core.platform.ViewBindingFragment
import com.skillbox.core.snackbar.CustomSnackbar
import com.skillbox.core.state.StateToolbar
import com.skillbox.shared_model.ToastModel
import com.skillbox.shared_model.ToolbarModel
import com.skillbox.shared_model.network.СreateActivity
import com.skillbox.strava.R
import com.skillbox.strava.databinding.FragmentActivitiesBinding
import com.skillbox.strava.ui.fragment.activities.adapter.itemRunnerCard
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ActivitiesFragment : ViewBindingFragment<FragmentActivitiesBinding>(FragmentActivitiesBinding::inflate) {

    override val screenViewModel by viewModels<ActivitiesViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        screenViewModel.runnerItemsObserver.observe(viewLifecycleOwner, { list ->
            list?.let {
                setAdapter(list)
            }
        })
        screenViewModel.loadDataObserver.observe(viewLifecycleOwner, { isLoad ->
            isLoad?.let {
                if(isLoad) {
                    binding.activitiesLoader.show()
                    binding.activitiesRecyclerView.gone()
                }
                else {
                    binding.activitiesLoader.gone()
                    binding.activitiesRecyclerView.show()
                }
            }
        })
        binding.activitiesFabButton.setOnClickListener {
            findNavController()
                    .navigate(R.id.action_activitiesFragment_to_addActivitiesFragment)
        }
        initList()
        screenViewModel.getAthleteActivities()
    }

    override fun onDestroyView() {
        screenViewModel.loadDataObserver.removeObserver {  }
        screenViewModel.runnerItemsObserver.removeObserver {  }
        super.onDestroyView()
    }

    override fun onStart() {
        super.onStart()
        StateToolbar.changeToolbarTitle(ToolbarModel("Activities"))
    }

    override fun localData(localToast: ToastModel) {
        if(localToast.text.isEmpty()) return
        CustomSnackbar.make(
                requireActivity().window.decorView.rootView as ViewGroup,
                localToast.isLocal,
                localToast.text,
                localToast.isError
        ) {
            screenViewModel.getAthleteActivities()
        }.show()
    }

    private val runnerCardAdapter by lazy {
        ListDelegationAdapter(
                itemRunnerCard(::share)
        )
    }

    private fun share() {

    }

    private fun initList() {
        with(binding.activitiesRecyclerView) {
            adapter = runnerCardAdapter
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
        }
    }

    private fun setAdapter(list: List<СreateActivity>) {
        runnerCardAdapter.setData(list)
    }
}