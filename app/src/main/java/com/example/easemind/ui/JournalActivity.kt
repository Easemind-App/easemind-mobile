package com.example.easemind.ui

import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.easemind.R
import com.example.easemind.databinding.ActivityJournalBinding
import com.example.easemind.ui.homepage.MainActivity
import com.example.easemind.ui.profile.ProfileActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

class JournalActivity : AppCompatActivity() {

    private lateinit var binding: ActivityJournalBinding
    lateinit var bottomNavigationView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityJournalBinding.inflate(layoutInflater)
        setContentView(binding.root)

        bottomNavigationView = binding.bottomNavView
        bottomNavigationView.selectedItemId = R.id.journal
        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.home -> {
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent, ActivityOptions.makeCustomAnimation(this, 0, 0).toBundle())
                    finish()
                }

                R.id.profile -> {
                    val intent = Intent(this, ProfileActivity::class.java)
                    startActivity(intent, ActivityOptions.makeCustomAnimation(this, 0, 0).toBundle())
                    finish()
                }
                R.id.journal -> {
                    true
                }
            }
            true
        }

    }
}