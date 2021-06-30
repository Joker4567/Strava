package com.skillbox.strava.ui.fragment.activities

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.hannesdorfmann.adapterdelegates4.ListDelegationAdapter
import com.skillbox.core.extensions.setData
import com.skillbox.core.platform.ViewBindingFragment
import com.skillbox.core.state.StateToolbar
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
        screenViewModel.runnerItemsObserver.observe(this, { list ->
            list?.let {
                setAdapter(list)
            }
        })
        binding.floatingActionButton.setOnClickListener {
            findNavController()
                    .navigate(R.id.action_activitiesFragment_to_addActivitiesFragment)
        }
        initList()
        screenViewModel.getAthleteActivities()
    }

    override fun onStart() {
        super.onStart()
        StateToolbar.changeToolbarTitle(ToolbarModel("Activities"))
    }

    private val runnerCardAdapter by lazy {
        ListDelegationAdapter(
                itemRunnerCard(::share)
        )
    }

    private fun share() {

    }

    private fun initList() {
        with(binding.searchRvSearch) {
            adapter = runnerCardAdapter
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
        }
    }

    private fun setAdapter(list: List<СreateActivity>) {
        runnerCardAdapter.setData(list)
    }
}