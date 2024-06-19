package com.example.easemind.ui.authentication

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.easemind.data.pref.UserModel
import com.example.easemind.databinding.ActivityAuthenticationBinding
import com.example.easemind.ui.homepage.MainActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task

class AuthenticationActivity : AppCompatActivity() {
    private lateinit var mGoogleSignInClient: GoogleSignInClient
    private lateinit var signInLauncher: ActivityResultLauncher<Intent>
    private lateinit var binding: ActivityAuthenticationBinding
    private val authenticationViewModel by viewModels<AuthenticationViewModel> {
        AuthenticationViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAuthenticationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)

        signInLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                Log.d("AuthenticationActivity", "Google sign-in successful")
                handleSignInResult(task)
            } else {
                Log.e("Google Sign-In Code", result.resultCode.toString())
                Log.e("Google Sign-In", "Sign-in failed")
            }
        }

        binding.btnGoogleLogin.setOnClickListener {
            signIn()
        }
    }

    private fun signIn() {
        val signInIntent = mGoogleSignInClient.signInIntent
        Log.d("AuthenticationActivity", "Launching Google sign-in intent")
        signInLauncher.launch(signInIntent)
    }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.getResult(
                ApiException::class.java
            )

            // Login Successful
            val googleFirstName = account?.givenName ?: ""
            Log.i("Google First Name", googleFirstName)
            val googleLastName = account?.familyName ?: ""
            Log.i("Google Last Name", googleLastName)
            val googleEmail = account?.email ?: ""
            Log.i("Google Email", googleEmail)
            val googleProfilePicURL = account?.photoUrl.toString()
            Log.i("Google Profile Pic URL", googleProfilePicURL)

            authenticationViewModel.login(googleEmail, googleFirstName)
            authenticationViewModel.loginUser.observe(this) {
                authenticationViewModel.saveSession(UserModel(googleEmail, googleFirstName, null, null, it.token, googleProfilePicURL))
                Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
            }

            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        } catch (e: ApiException) {
            // Login Failed
            Log.e(
                "failed code=", e.statusCode.toString()
            )
        }
    }

}