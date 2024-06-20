package com.example.easemind.ui.questionnaire

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.example.easemind.R
import com.example.easemind.databinding.FragmentFaceRecognationBinding
import com.example.easemind.databinding.FragmentResultBinding
import com.example.easemind.ui.authentication.AuthenticationViewModel
import com.example.easemind.ui.authentication.AuthenticationViewModelFactory
import com.example.easemind.ui.homepage.MainActivity
import com.example.easemind.ui.journal.DetailJournalActivity
import com.example.easemind.ui.journal.JournalActivity

class ResultFragment : Fragment() {

    private lateinit var binding: FragmentResultBinding
    private val authenticationViewModel by viewModels<AuthenticationViewModel> {
        AuthenticationViewModelFactory.getInstance(requireActivity())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentResultBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        authenticationViewModel.addJournalResult.observe(viewLifecycleOwner) { result ->
            result?.let {
//                // Update UI to show the result of the added journal
//                Glide.with(requireActivity())
//                    .load(R.drawable.draw_overjoyed)
//                    .into(binding.ivItemEmoji) //TODO: create categorization
//                binding.dateOfJournal.text = result.journals.result
//                binding.tvItemCategory.text = journal.result
//                binding.thoughtValue.text = journal.thoughts
            }
        }

        binding.arrowBack.setOnClickListener {
            requireActivity().onBackPressed()
        }

        binding.home.setOnClickListener {
            val intent = Intent(requireActivity(), MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            requireActivity().finish()
        }

        binding.listJournal.setOnClickListener {
            val intent = Intent(requireActivity(), JournalActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            requireActivity().finish()
        }
    }
}
