package com.example.easemind

import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

class ProfileActivity : AppCompatActivity() {

    lateinit var bottomNavigationView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

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

        bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_nav_view)
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