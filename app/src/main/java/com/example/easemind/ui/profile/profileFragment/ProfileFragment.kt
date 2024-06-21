package com.example.easemind.ui.profile.profileFragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.example.easemind.R
import com.example.easemind.data.response.UserResponse
import com.example.easemind.databinding.FragmentProfileBinding
import com.example.easemind.ui.authentication.AuthenticationActivity
import com.example.easemind.ui.authentication.AuthenticationViewModel
import com.example.easemind.ui.authentication.AuthenticationViewModelFactory
import com.example.easemind.ui.profile.editProfileFragment.EditProfileFragment
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions

class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding
    private val profileViewModel: ProfileViewModel by viewModels {
        ProfileViewModelFactory.getInstance(requireContext())
    }
    private val authenticationViewModel: AuthenticationViewModel by viewModels {
        AuthenticationViewModelFactory.getInstance(requireContext())
    }
    private lateinit var mGoogleSignInClient: GoogleSignInClient

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()
        mGoogleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)

        setupViews()
        observeViewModel()
    }

    private fun signOut() {
        mGoogleSignInClient.signOut()
            .addOnCompleteListener(requireActivity()) {
                val intent = Intent(activity, AuthenticationActivity::class.java)
                startActivity(intent)
            }
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
            bundle.putString("age", binding.ageValue.text.toString().removeSuffix(" years old"))
            bundle.putString("gender", binding.genderValue.text.toString())
            editProfileFragment.arguments = bundle

            parentFragmentManager.beginTransaction().apply {
                replace(R.id.container, editProfileFragment)
                addToBackStack(null)
                commit()
            }
        }

        binding.btnLogout.setOnClickListener {
            signOut()
        }
    }

    private fun observeViewModel() {
        authenticationViewModel.getSession().observe(viewLifecycleOwner) { userSession ->
            Glide.with(this)
                .load(userSession.profilePicture)
                .into(binding.imageProfile)

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
            binding.ageValue.text = "${it.age} years old"
            binding.genderValue.text = when (it.gender) {
                "L" -> "Male"
                "P" -> "Female"
                else -> it.gender
            }
        }
    }
}
