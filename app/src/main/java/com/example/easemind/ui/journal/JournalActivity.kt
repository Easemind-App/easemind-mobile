package com.example.easemind.ui.journal

import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.easemind.R
import com.example.easemind.data.response.JournalsItem
import com.example.easemind.databinding.ActivityJournalBinding
import com.example.easemind.ui.authentication.AuthenticationViewModel
import com.example.easemind.ui.authentication.AuthenticationViewModelFactory
import com.example.easemind.ui.homepage.MainActivity
import com.example.easemind.ui.profile.ProfileActivity
import com.example.easemind.ui.questionnaire.QuestionnaireActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

class JournalActivity : AppCompatActivity() {
    private val authenticationViewModel by viewModels<AuthenticationViewModel> {
        AuthenticationViewModelFactory.getInstance(this)
    }
    private lateinit var binding: ActivityJournalBinding
    lateinit var bottomNavigationView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityJournalBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val layoutManager = LinearLayoutManager(this)
        binding.rvJournal.layoutManager = layoutManager

        authenticationViewModel.getSession().observe(this) { user ->
            authenticationViewModel.getJournal(user.token)
            authenticationViewModel.journal.observe(this) {
                setJournal(it)
            }
            authenticationViewModel.isLoading.observe(this) {
                showLoading(it)
            }
        }

        binding.fab.setOnClickListener {
            val intent = Intent(this, QuestionnaireActivity::class.java)
            startActivity(intent)
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

    private fun setJournal(journals: List<JournalsItem>) {
        val adapter = JournalAdapter()
        adapter.submitList(journals)
        binding.rvJournal.adapter = adapter
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility  = if (isLoading) View.VISIBLE else View.GONE
    }

}