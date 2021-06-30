package com.skillbox.strava.ui.fragment.contact

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.skillbox.core.platform.ViewBindingFragment
import com.skillbox.core.state.StateToolbar
import com.skillbox.core.utils.autoCleared
import com.skillbox.shared_model.ToolbarModel
import com.skillbox.shared_model.contact.Contact
import com.skillbox.strava.databinding.FragmentContactBinding
import com.skillbox.strava.ui.fragment.contact.adapter.ContactAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ContactFragment : ViewBindingFragment<FragmentContactBinding>(FragmentContactBinding::inflate) {

    override val screenViewModel by viewModels<ContactViewModel>()
    private var contactAdapter: ContactAdapter by autoCleared()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        screenViewModel.contactObserver.observe(viewLifecycleOwner, { items ->
            items?.let {
                contactAdapter.items = it as List<Contact>
            }
        })
        checkPermissions()
        contactAdapter =
                ContactAdapter(onItemClick = { contact ->
                    openContactDetailInfo(contact)
                })
        binding.contactRecycler.apply {
            adapter = contactAdapter
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
        }
    }

    override fun onStart() {
        super.onStart()
        StateToolbar.changeToolbarTitle(ToolbarModel("Share", visibleLogOut = false))
    }

    override fun onDestroyView() {
        screenViewModel.contactObserver.removeObserver {  }
        super.onDestroyView()
    }

    private fun openContactDetailInfo(contact: Contact) {

    }

    private fun checkPermissions() {
        val readGranted = ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.READ_CONTACTS
        ) == PackageManager.PERMISSION_GRANTED

        val writeGranted = ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.WRITE_CONTACTS
        ) == PackageManager.PERMISSION_GRANTED

        if (readGranted.not() && writeGranted.not()) {
            requestPermissions(
                    arrayOf(Manifest.permission.READ_CONTACTS, Manifest.permission.WRITE_CONTACTS),
                    PERMISSIONS_REQUEST_CODE
            )
        } else {
            screenViewModel.getContacts(requireContext())
        }
    }

    override fun onRequestPermissionsResult(
            requestCode: Int,
            permissions: Array<out String>,
            grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (grantResults.all { it == PackageManager.PERMISSION_GRANTED }) {
            screenViewModel.getContacts(requireContext())
        } else {
            requestPermissions(
                    arrayOf(Manifest.permission.READ_CONTACTS, Manifest.permission.WRITE_CONTACTS),
                    PERMISSIONS_REQUEST_CODE
            )
        }
    }

    companion object {
        private const val PERMISSIONS_REQUEST_CODE = 214
    }
}