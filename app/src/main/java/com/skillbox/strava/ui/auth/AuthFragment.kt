package com.skillbox.strava.ui.auth

import android.os.Bundle
import android.view.View
import com.skillbox.core_network.ConstAPI
import com.skillbox.core.platform.ViewBindingFragment
import com.skillbox.strava.databinding.FragmentAuthBinding
import dagger.hilt.android.AndroidEntryPoint

import android.net.Uri
import net.openid.appauth.AuthorizationServiceConfiguration
import net.openid.appauth.ResponseTypeValues

import net.openid.appauth.AuthorizationRequest

import android.util.Log

import net.openid.appauth.AuthorizationService

@AndroidEntryPoint
class AuthFragment : ViewBindingFragment<FragmentAuthBinding>(FragmentAuthBinding::inflate) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.authButton.setOnClickListener {
            doAuthorization()
        }
    }

    private fun doAuthorization() {
        val authUrl = "http://www.strava.com/oauth/authorize"

        val serviceConfig = AuthorizationServiceConfiguration(
                Uri.parse(authUrl),
                Uri.parse("https://www.strava.com/oauth/token"))

        val authRequest = AuthorizationRequest.Builder(
                serviceConfig,  // the authorization service configuration
                ConstAPI.id_client.toString(),  // the client ID, typically pre-registered and static
                ResponseTypeValues.CODE,  // the response_type value: we want a code
                Uri.parse("https://strava/token")).build() // the redirect URI to which the auth response is sent

        val authService = AuthorizationService(requireContext())
        val authIntent = authService.getAuthorizationRequestIntent(authRequest)
        if(authIntent.resolveActivity(requireContext().packageManager) != null) {
            startActivity(authIntent)
        } else {
            Log.e("AuthFragment", "Не могу обработать открытие activity, не найден browser for phone")
        }
    }
}