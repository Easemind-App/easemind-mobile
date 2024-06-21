package com.example.easemind.ui.homepage

import com.example.easemind.ui.questionnaire.QuestionnaireActivity
import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.easemind.ui.authentication.AuthenticationActivity
import com.example.easemind.ui.journal.JournalActivity
import com.example.easemind.R
import com.example.easemind.data.response.JournalsItem
import com.example.easemind.databinding.ActivityMainBinding
import com.example.easemind.ui.authentication.AuthenticationViewModel
import com.example.easemind.ui.authentication.AuthenticationViewModelFactory
import com.example.easemind.ui.profile.ProfileActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions

class MainActivity : AppCompatActivity() {
    private lateinit var mGoogleSignInClient: GoogleSignInClient
    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var binding: ActivityMainBinding
    private val authenticationViewModel by viewModels<AuthenticationViewModel> {
        AuthenticationViewModelFactory.getInstance(context = this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)


        authenticationViewModel.getSession().observe(this) { user ->
            val username = user.username
            binding.name.text = getString(R.string.name, username)
            Glide.with(this)
                .load(user.profilePicture)
                .into(this.binding.imageProfile)

            val token = user.token
            authenticationViewModel.getJournal(token)
            authenticationViewModel.journal.observe(this) { journals ->
                displayJournals(journals)
            }

            authenticationViewModel.journalCheckpoint.observe(this) {
                setCheckupButton(it)
            }

            authenticationViewModel.isLoading.observe(this) { isLoading ->
                showLoading(isLoading)
            }
        }

        binding.checkupButton.setOnClickListener {
            val intent = Intent(this, QuestionnaireActivity::class.java)
            startActivity(intent)
        }

        bottomNavigationView = binding.bottomNavView
        bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_nav_view)
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

    private fun displayJournals(journals: List<JournalsItem>) {
        val noJournalMessage = findViewById<TextView>(R.id.no_journal_message)
        val progressBar = findViewById<ProgressBar>(R.id.progressBar)
        val cardViewTrackingContent = findViewById<LinearLayout>(R.id.card_view_tracking_content)

        if (journals.isEmpty()) {
            noJournalMessage.visibility = View.VISIBLE
            cardViewTrackingContent.visibility = View.GONE
        } else {
            noJournalMessage.visibility = View.GONE
            cardViewTrackingContent.visibility = View.VISIBLE
            cardViewTrackingContent.removeAllViews()

            journals.takeLast(5).forEach { journal ->
                val itemRecap = ItemRecap(this)
                itemRecap.setDateText(journal.journalDate)
                itemRecap.setEmojiDrawable(R.drawable.draw_overjoyed)

                val layoutParams = LinearLayout.LayoutParams(
                    0,
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    1f
                )
                itemRecap.layoutParams = layoutParams
                cardViewTrackingContent.addView(itemRecap)
            }
        }
        progressBar.visibility = View.GONE
    }

    private fun setCheckupButton(checkpoint: Boolean) {
        binding.checkupButton.isEnabled = checkpoint
    }

    private fun showLoading(isLoading: Boolean) {
        val progressBar = findViewById<ProgressBar>(R.id.progressBar)
        val cardViewTrackingContent = findViewById<View>(R.id.card_view_tracking_content)
        val noJournalMessage = findViewById<TextView>(R.id.no_journal_message)

        if (isLoading) {
            progressBar.visibility = View.VISIBLE
            cardViewTrackingContent.visibility = View.GONE
            noJournalMessage.visibility = View.GONE
        } else {
            progressBar.visibility = View.GONE
        }
    }
}
