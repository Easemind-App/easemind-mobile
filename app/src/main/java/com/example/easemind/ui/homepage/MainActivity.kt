package com.example.easemind.ui.homepage

import com.example.easemind.ui.questionnaire.QuestionnaireActivity
import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
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

    private fun signOut() {
        mGoogleSignInClient.signOut()
            .addOnCompleteListener(this) {
                val intent = Intent(this, AuthenticationActivity::class.java)
                startActivity(intent)
                finish()
            }
    }

    private fun displayJournals(journals: List<JournalsItem>) {
        val imageViewIds = listOf(
            R.id.iv_item_emoji_1,
            R.id.iv_item_emoji_2,
            R.id.iv_item_emoji_3,
            R.id.iv_item_emoji_4,
            R.id.iv_item_emoji_5,
        )

        val textViewIds = listOf(
            R.id.date_1,
            R.id.date_2,
            R.id.date_3,
            R.id.date_4,
            R.id.date_5,
        )

        // Clear any previous content
        imageViewIds.forEach { id ->
            findViewById<ImageView>(id).setImageResource(0) // Clear image
        }
        textViewIds.forEach { id ->
            findViewById<TextView>(id).text = "" // Clear text
        }

        val noJournalMessage = findViewById<TextView>(R.id.no_journal_message)
        val progressBar = findViewById<ProgressBar>(R.id.progressBar)
        val cardViewTracking = findViewById<View>(R.id.card_view_tracking_content)

        if (journals.isEmpty()) {
            noJournalMessage.visibility = View.VISIBLE
            cardViewTracking.visibility = View.GONE
        } else {
            noJournalMessage.visibility = View.GONE
            cardViewTracking.visibility = View.VISIBLE

            journals.takeLast(5).forEachIndexed { index, journal ->
                val imageView = findViewById<ImageView>(imageViewIds[index])
                Glide.with(this)
                    .load(R.drawable.draw_overjoyed)
                    .into(imageView)

                val textView = findViewById<TextView>(textViewIds[index])
                textView?.text = journal.journalDate
            }
        }
        progressBar.visibility = View.GONE
    }

    private fun showLoading(isLoading: Boolean) {
        val progressBar = findViewById<ProgressBar>(R.id.progressBar)
        progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}