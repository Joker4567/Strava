package com.skillbox.strava.ui.fragment.auth

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.app.ActivityCompat.finishAffinity
import androidx.fragment.app.viewModels
import com.skillbox.core.platform.ViewBindingFragment
import com.skillbox.core_db.pref.Pref
import com.skillbox.core_network.ConstAPI
import com.skillbox.shared_model.ToastModel
import com.skillbox.strava.databinding.FragmentAuthBinding
import com.skillbox.strava.ui.activity.main.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import net.openid.appauth.AuthorizationRequest
import net.openid.appauth.AuthorizationService
import net.openid.appauth.AuthorizationServiceConfiguration
import net.openid.appauth.ResponseTypeValues

@AndroidEntryPoint
class AuthFragment : ViewBindingFragment<FragmentAuthBinding>(FragmentAuthBinding::inflate) {

    override val screenViewModel by viewModels<AuthViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.authButton.setOnClickListener {
            doAuthorization()
        }
        screenViewModel.getIsAthlete()
    }

    override fun localData(localToast: ToastModel) {
        if(localToast.isLocal.not() && Pref(requireContext()).accessToken.isNotEmpty()){
            val intent = Intent(requireActivity(), MainActivity::class.java)
            startActivity(intent)
            finishAffinity(requireActivity())
        }
    }

    private fun doAuthorization() {
        val intentUri : Uri = Uri.parse("https://www.strava.com/oauth/mobile/authorize")
                .buildUpon()
                .appendQueryParameter("client_id", ConstAPI.id_client.toString())
                .appendQueryParameter("redirect_uri", "https://www.strava.com/oauth/token")
                .appendQueryParameter("response_type", "code")
                .appendQueryParameter("approval_prompt", "auto")
                .appendQueryParameter("scope", "read,activity:write,activity:read,profile:write,profile:read_all")
                .build()

        val serviceConfig = AuthorizationServiceConfiguration(
                intentUri,
                Uri.parse("https://www.strava.com/oauth/token"))

        val authRequest = AuthorizationRequest.Builder(
                serviceConfig,
                ConstAPI.id_client.toString(),
                ResponseTypeValues.CODE,
                Uri.parse("https://strava/token")).build()

        val authService = AuthorizationService(requireContext())
        val authIntent = authService.getAuthorizationRequestIntent(authRequest)
        if(authIntent.resolveActivity(requireContext().packageManager) != null) {
            startActivity(authIntent)
        } else {
            Log.e("AuthFragment", "Не могу обработать открытие activity, не найден browser for phone")
        }
    }
}