package com.example.easemind.ui.homepage

import com.example.easemind.ui.questionnaire.QuestionnaireActivity
import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.easemind.ui.journal.JournalActivity
import com.example.easemind.R
import com.example.easemind.databinding.ActivityMainBinding
import com.example.easemind.ui.profile.ProfileActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private  lateinit var binding: ActivityMainBinding
    lateinit var bottomNavigationView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.checkupButton.setOnClickListener {
            val intent = Intent(this, QuestionnaireActivity::class.java)
            startActivity(intent)
        }

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
                }
                R.id.profile -> {
                    val intent = Intent(this, ProfileActivity::class.java)
                    startActivity(intent, ActivityOptions.makeCustomAnimation(this, 0, 0).toBundle())
                    finish()
                }
            }
            true
        }

    }
}