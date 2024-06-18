package com.example.easemind.ui.journal

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.example.easemind.R
import com.example.easemind.databinding.ActivityDetailJournalBinding

class DetailJournalActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailJournalBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailJournalBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Glide.with(this).load(intent.getStringExtra(EXTRA_EMOTION)).into(binding.resultEmoji)
        binding.result.text = intent.getStringExtra(EXTRA_CATEGORY)
        binding.dateOfJournal.text = intent.getStringExtra(EXTRA_DATE)
        binding.thoughtValue.text = intent.getStringExtra(EXTRA_THOUGHTS)
    }

    companion object {
        const val EXTRA_EMOTION = "extra_emotion"
        const val EXTRA_CATEGORY = "extra_category"
        const val EXTRA_DATE = "extra_date"
        const val EXTRA_THOUGHTS = "extra_thoughts"
    }
}