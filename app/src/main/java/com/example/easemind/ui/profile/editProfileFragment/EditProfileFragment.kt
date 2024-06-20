package com.example.easemind.ui.profile.editProfileFragment

import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.easemind.R
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
        val gender = resources.getStringArray(R.array.gender)
        val arrayAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_gender, gender)
        binding.genderValue.setAdapter(arrayAdapter)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViews()
        parseArguments()
    }

    private fun parseArguments() {
        authenticationViewModel.getSession().observe(viewLifecycleOwner) { userSession ->
            Glide.with(this)
                .load(userSession.profilePicture)
                .into(binding.imageProfile)
        }

        arguments?.let {
            val username = it.getString("username")
            val email = it.getString("email")
            val age = it.getString("age")
            val gender = it.getString("gender")

            binding.name.text = Editable.Factory.getInstance().newEditable(username)
            binding.email.text = email
            binding.ageValue.text = Editable.Factory.getInstance().newEditable(age)
            binding.genderValue.setText(gender, false)
        }
    }

    private fun setupViews() {
        binding.save.setOnClickListener {
            editProfile()
        }

        binding.arrowBack.setOnClickListener {
            parentFragmentManager.popBackStack()
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
        val userName = binding.name.text?.toString()?.takeIf { it.isNotBlank() }
        val age = binding.ageValue.text?.toString()
        val genderText = binding.genderValue.text?.toString()
        val gender = if (genderText == "Male") "L" else "P"

        lifecycleScope.launch {
            editProfileViewModel.editProfile(userName, age, gender)
        }
    }
}
