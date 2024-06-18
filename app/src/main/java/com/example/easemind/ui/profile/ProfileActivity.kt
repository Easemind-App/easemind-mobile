package com.example.easemind.ui.profile

import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.easemind.ui.journal.JournalActivity
import com.example.easemind.ui.homepage.MainActivity
import com.example.easemind.R
import com.example.easemind.databinding.ActivityProfileBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class ProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProfileBinding
    lateinit var bottomNavigationView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val fragmentManager = supportFragmentManager
        val profileFragment = ProfileFragment()
        val fragment = fragmentManager.findFragmentByTag(ProfileFragment::class.java.simpleName)

        if (fragment !is ProfileFragment) {
            Log.d("MyFragment", "Fragment Name :" + ProfileFragment::class.java.simpleName)
            fragmentManager
                .beginTransaction()
                .add(R.id.container, profileFragment, ProfileFragment::class.java.simpleName)
                .commit()
        }

        bottomNavigationView = binding.bottomNavView
        bottomNavigationView.selectedItemId = R.id.profile
        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.journal -> {
                    val intent = Intent(this, JournalActivity::class.java)
                    startActivity(intent, ActivityOptions.makeCustomAnimation(this, 0, 0).toBundle())
                    finish()
                }
                R.id.home -> {
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent, ActivityOptions.makeCustomAnimation(this, 0, 0).toBundle())
                    finish()
                }
                R.id.profile -> {
                    true
                }
            }
            true
        }

    }

}