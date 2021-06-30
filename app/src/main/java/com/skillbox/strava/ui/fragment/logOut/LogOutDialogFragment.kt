package com.skillbox.strava.ui.fragment.logOut

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat.finishAffinity
import androidx.fragment.app.viewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.skillbox.strava.R
import com.skillbox.strava.ui.activity.OnBoardingActivity
import com.skillbox.strava.ui.fragment.auth.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.bottom_sheet_dialog.*

@AndroidEntryPoint
class LogOutDialogFragment : BottomSheetDialogFragment() {

    private val screenViewModel by viewModels<LogOutViewMode>()

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        screenViewModel.reAuthStateObserver.observe(viewLifecycleOwner, { isSuccess ->
            isSuccess?.let {
                if(isSuccess)
                {
                    startActivity(Intent(requireActivity(), OnBoardingActivity::class.java))
                    finishAffinity(requireActivity())
                }
            }
        })
        bottom_no.setOnClickListener {
            dismiss()
        }
        bottom_yes.setOnClickListener {
            screenViewModel.exit()
        }
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.bottom_sheet_dialog, container, false)
    }
}