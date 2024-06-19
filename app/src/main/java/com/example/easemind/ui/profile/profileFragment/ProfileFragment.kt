package com.example.easemind.ui.profile.profileFragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.viewModels
import com.example.easemind.R
import com.example.easemind.data.response.UserResponse
import com.example.easemind.databinding.FragmentProfileBinding
import com.example.easemind.ui.authentication.AuthenticationViewModel
import com.example.easemind.ui.authentication.AuthenticationViewModelFactory
import com.example.easemind.ui.profile.editProfileFragment.EditProfileFragment

class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding
    private val profileViewModel: ProfileViewModel by viewModels {
        ProfileViewModelFactory.getInstance(requireContext())
    }
    private val authenticationViewModel: AuthenticationViewModel by viewModels {
        AuthenticationViewModelFactory.getInstance(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViews()
        observeViewModel()
    }

    private fun setupViews() {
        val btnEditProfile: Button = binding.editProfile
        btnEditProfile.setOnClickListener {
            Log.d("ProfileFragment", "Edit Profile button clicked")
            val editProfileFragment = EditProfileFragment()

            // Mengirimkan data pengguna ke EditProfileFragment
            val bundle = Bundle()
            bundle.putString("username", binding.name.text.toString())
            bundle.putString("email", binding.email.text.toString())
            bundle.putString("age", binding.ageValue.text.toString())
            bundle.putString("gender", binding.genderValue.text.toString())
            editProfileFragment.arguments = bundle

            parentFragmentManager.beginTransaction().apply {
                replace(R.id.container, editProfileFragment)
                addToBackStack(null)
                commit()
            }
        }
    }

    private fun observeViewModel() {
        authenticationViewModel.getSession().observe(viewLifecycleOwner) { userSession ->
            userSession?.let {
                profileViewModel.getUserProfile()
            }
        }

        profileViewModel.userProfile.observe(viewLifecycleOwner) { userProfile ->
            updateProfileUI(userProfile)
        }

    }

    private fun updateProfileUI(userProfile: UserResponse?) {
        userProfile?.let {
            binding.name.text = it.userName
            binding.email.text = it.email
            binding.ageValue.text = it.age
            binding.genderValue.text = when (it.gender) {
                "L" -> "Laki-laki"
                "P" -> "Perempuan"
                else -> it.gender
            }
        }
    }
}