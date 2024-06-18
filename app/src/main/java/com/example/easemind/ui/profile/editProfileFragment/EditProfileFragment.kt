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

        observeSession()
        setupViews()
    }

    private fun observeSession() {
        authenticationViewModel.getSession().observe(viewLifecycleOwner) { user ->
            this.user = user
            binding.name.text = user.username
            binding.ageValue.setText(user.age)
            binding.genderValue.setText(user.gender)
        }
    }

    private fun setupViews() {
        binding.save.setOnClickListener {
            editProfile()
        }

        editProfileViewModel.editProfile.observe(viewLifecycleOwner) { editProfileResponse ->
            if (editProfileResponse != null) {
                Toast.makeText(requireContext(), "Profile changed successfully", Toast.LENGTH_SHORT).show()
                parentFragmentManager.popBackStack()
            } else {
                Toast.makeText(requireContext(), "Failed to change profile", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun editProfile() {
        val username = binding.name.text?.toString()
        val age = binding.ageValue.text?.toString()
        val gender = binding.genderValue.text?.toString()

        lifecycleScope.launch {
            editProfileViewModel.editProfile(username, age, gender)
        }
    }
}
