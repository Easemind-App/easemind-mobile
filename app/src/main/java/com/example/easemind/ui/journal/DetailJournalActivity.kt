package com.example.easemind.ui.journal

import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.example.easemind.R
import com.example.easemind.databinding.ActivityDetailJournalBinding
import com.example.easemind.ui.homepage.MainActivity
import com.example.easemind.ui.profile.ProfileActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

class DetailJournalActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailJournalBinding
    private lateinit var bottomNavigationView: BottomNavigationView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailJournalBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Glide.with(this).load(intent.getStringExtra(EXTRA_EMOTION)).into(binding.resultEmoji)
        binding.result.text = intent.getStringExtra(EXTRA_CATEGORY)
        binding.dateOfJournal.text = intent.getStringExtra(EXTRA_DATE)
        binding.thoughtValue.text = intent.getStringExtra(EXTRA_THOUGHTS)

        val arrowBack = binding.arrowBack
        arrowBack.setOnClickListener {
            finish()
        }

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

    companion object {
        const val EXTRA_EMOTION = "extra_emotion"
        const val EXTRA_CATEGORY = "extra_category"
        const val EXTRA_DATE = "extra_date"
        const val EXTRA_THOUGHTS = "extra_thoughts"
    }
}