package com.example.easemind.ui.homepage

import com.example.easemind.ui.questionnaire.QuestionnaireActivity
import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.easemind.ui.authentication.AuthenticationActivity
import com.example.easemind.ui.journal.JournalActivity
import com.example.easemind.R
import com.example.easemind.databinding.ActivityMainBinding
import com.example.easemind.ui.authentication.AuthenticationViewModel
import com.example.easemind.ui.authentication.AuthenticationViewModelFactory
import com.example.easemind.ui.profile.ProfileActivity
import com.google.android.material.bottomnavigation.BottomNavigationView


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var bottomNavigationView: BottomNavigationView

    private val userViewModel by viewModels<AuthenticationViewModel> {
        AuthenticationViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userViewModel.getSession().observe(this) { user ->
            if (!user.isLogin) {
                Log.d("MainActivity", "User not logged in")
                startActivity(Intent(this, AuthenticationActivity::class.java))
                finish() // Menutup MainActivity jika pengguna belum login
            } else {
                Log.d("MainActivity", "User logged in")
                initViews()
            }
        }
    }

    private fun initViews() {
        bottomNavigationView = binding.bottomNavView
        bottomNavigationView.selectedItemId = R.id.home
        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.home -> {
                    true
                }
                R.id.journal -> {
                    val intent = Intent(this, JournalActivity::class.java)
                    startActivity(intent, ActivityOptions.makeCustomAnimation(this, 0, 0).toBundle())
                    finish()
                    true
                }
                R.id.profile -> {
                    val intent = Intent(this, ProfileActivity::class.java)
                    startActivity(intent, ActivityOptions.makeCustomAnimation(this, 0, 0).toBundle())
                    finish()
                    true
                }
                else -> false
            }
        }
    }

}
