package com.skillbox.strava.ui.auth

import android.os.Bundle
import android.view.View
import com.skillbox.core.ConstAPI
import com.skillbox.core.platform.ViewBindingFragment
import com.skillbox.strava.databinding.FragmentAuthBinding
import dagger.hilt.android.AndroidEntryPoint

import android.net.Uri
import net.openid.appauth.AuthorizationServiceConfiguration
import net.openid.appauth.AuthState
import net.openid.appauth.ResponseTypeValues

import net.openid.appauth.AuthorizationRequest

import android.content.Intent
import android.util.Log

import net.openid.appauth.AuthorizationService
import net.openid.appauth.AuthorizationException

import net.openid.appauth.AuthorizationResponse

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
        val authState = AuthState(serviceConfig)

        val authRequest = AuthorizationRequest.Builder(
                serviceConfig,  // the authorization service configuration
                ConstAPI.id_client.toString(),  // the client ID, typically pre-registered and static
                ResponseTypeValues.CODE,  // the response_type value: we want a code
                Uri.parse("https://strava/token")).build() // the redirect URI to which the auth response is sent

        val authService = AuthorizationService(requireContext())
        val authIntent = authService.getAuthorizationRequestIntent(authRequest)
        startActivityForResult(authIntent, 456)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 456) {
            data?.let {
                val resp = AuthorizationResponse.fromIntent(data)
                val ex = AuthorizationException.fromIntent(data)
                Log.d("", "")
            }
        }
    }
}