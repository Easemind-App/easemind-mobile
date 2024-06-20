package com.example.easemind.ui.questionnaire

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.activity.viewModels
import androidx.fragment.app.viewModels
import com.example.easemind.R
import com.example.easemind.databinding.FragmentInputFeelingsBinding
import com.example.easemind.ui.authentication.AuthenticationViewModel
import com.example.easemind.ui.authentication.AuthenticationViewModelFactory
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class InputFeelingsFragment : Fragment(){

    private lateinit var binding: FragmentInputFeelingsBinding
    private val authenticationViewModel by viewModels<AuthenticationViewModel> {
        AuthenticationViewModelFactory.getInstance(requireActivity())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentInputFeelingsBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.arrowBack.setOnClickListener {
            requireActivity().onBackPressed()
        }

        binding.analyze.setOnClickListener{
            Log.d("InputFeelingsFragment", "Analyze button clicked")

            // Ambil tanggal hari ini
            val currentDate = getCurrentDate()

            // Ambil isi dari TextInputEditText
            val thoughts = binding.textFieldFeeling.editText?.text.toString()

            // Panggil fungsi addJournal dari ViewModel untuk menambahkan jurnal
            authenticationViewModel.addJournal(currentDate, "Neutral", thoughts)

            // Navigate ke ResultFragment setelah menambahkan jurnal
            val resultFragment = ResultFragment()
            parentFragmentManager.beginTransaction().apply {
                replace(R.id.container, resultFragment, ResultFragment::class.java.simpleName)
                addToBackStack(null)
                commit()
            }
        }
    }

    private fun getCurrentDate(): String {
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        return dateFormat.format(Date())
    }


}