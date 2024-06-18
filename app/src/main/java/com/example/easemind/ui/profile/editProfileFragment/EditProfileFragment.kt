package com.example.easemind.ui.profile.editProfileFragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.easemind.data.pref.UserModel
import com.example.easemind.databinding.FragmentEditProfileBinding
import com.example.easemind.ui.authentication.AuthenticationViewModel
import com.example.easemind.ui.authentication.AuthenticationViewModelFactory
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch

class EditProfileFragment : Fragment() {

    private lateinit var binding: FragmentEditProfileBinding
    private val editProfileViewModel: EditProfileViewModel by viewModels {
        EditProfileViewModelFactory.getInstance(requireActivity())
    }
    private val authenticationViewModel by viewModels<AuthenticationViewModel> {
        AuthenticationViewModelFactory.getInstance(requireActivity())
    }
    private lateinit var user: UserModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentEditProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViews()
        observeViewModel()
    }

    private fun setupViews() {
        binding.save.setOnClickListener{editProfile()}
        authenticationViewModel.getSession().observe(requireActivity()) {
            user = it
            binding.name.setText(it.username)
            binding.age.setText(it.age)
            binding.gender.setText(it.gender)
        }

        editProfileViewModel.editProfile.observe(viewLifecycleOwner) { editProfileResponse ->
            if (editProfileResponse != null) {
                Toast.makeText(requireContext(), "Profile changed successfully", Toast.LENGTH_SHORT).show()
                parentFragmentManager.popBackStack()
            } else {
                Toast.makeText(requireContext(), "Failed to change profile", Toast.LENGTH_SHORT).show()
            }
        }

        editProfileViewModel.isLoading.observe(requireActivity()) { isLoading ->
            if (isLoading) {
                showLoading(true)
            } else {
                showLoading(false)
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun observeViewModel() {
        editProfileViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            if (isLoading) {
                binding.progressBar.visibility = View.VISIBLE
            } else {
                binding.progressBar.visibility = View.GONE
            }
        }

        editProfileViewModel.editProfile.observe(viewLifecycleOwner) {
            // Handle edit profile response if needed
            Snackbar.make(binding.root, "Profile updated successfully", Snackbar.LENGTH_SHORT)
                .show()
        }
    }

    private fun editProfile() {
        val username = binding.name.text?.toString()
        val age = binding.editTextAge.text?.toString()
        val gender = binding.gender.text?.toString()

        lifecycleScope.launch {
            editProfileViewModel.editProfile(username, age, gender)
        }

    }

}
