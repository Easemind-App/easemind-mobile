package com.example.easemind.ui.questionnaire

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import com.example.easemind.R

class QuestionSleepPatternsFragment : Fragment(), View.OnClickListener  {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_question_sleep_patterns, container, false)

        val arrowBack = view.findViewById<ImageView>(R.id.arrow_back)
        arrowBack.setOnClickListener {
            // Handle the back navigation
            parentFragmentManager.popBackStack()
        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val btnContinue: Button = view.findViewById(R.id.next)
        btnContinue.setOnClickListener(this)
    }
//    override fun onClick(v: View) {
//        if (v.id == R.id.next) {
//            Log.d("QuestionSleeppPatternsFragment", "Next button clicked")
//            val faceRecognationFragment = FaceRecognationFragment()
//            val fragmentManager = parentFragmentManager
//            fragmentManager.beginTransaction().apply {
//                replace(R.id.container, faceRecognationFragment, FaceRecognationFragment::class.java.simpleName)
//                addToBackStack(null)
//                commit()
//            }
//        }
//    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.very_poor_btn -> {
                toggleButtonBackground(v as AppCompatButton)
                // Handle logic for very poor button click
            }
            R.id.poor_btn -> {
                toggleButtonBackground(v as AppCompatButton)
                // Handle logic for poor button click
            }
            R.id.average_btn -> {
                toggleButtonBackground(v as AppCompatButton)
                // Handle logic for average button click
            }
            R.id.good_btn -> {
                toggleButtonBackground(v as AppCompatButton)
                // Handle logic for good button click
            }
            R.id.excellent_btn -> {
                toggleButtonBackground(v as AppCompatButton)
                // Handle logic for excellent button click
            }
            R.id.next -> {
                Log.d("QuestionSleeppPatternsFragment", "Next button clicked")
                val faceRecognationFragment = FaceRecognationFragment()
                val fragmentManager = parentFragmentManager
                fragmentManager.beginTransaction().apply {
                    replace(R.id.container, faceRecognationFragment, FaceRecognationFragment::class.java.simpleName)
                    addToBackStack(null)
                    commit()
                }
            }
        }
    }

    private fun toggleButtonBackground(button: AppCompatButton) {
        // Reset all button backgrounds
        view?.findViewById<AppCompatButton>(R.id.very_poor_btn)?.backgroundTintList =
            ContextCompat.getColorStateList(requireContext(), R.color.white)
        view?.findViewById<AppCompatButton>(R.id.poor_btn)?.backgroundTintList =
            ContextCompat.getColorStateList(requireContext(), R.color.white)
        view?.findViewById<AppCompatButton>(R.id.average_btn)?.backgroundTintList =
            ContextCompat.getColorStateList(requireContext(), R.color.white)
        view?.findViewById<AppCompatButton>(R.id.good_btn)?.backgroundTintList =
            ContextCompat.getColorStateList(requireContext(), R.color.white)
        view?.findViewById<AppCompatButton>(R.id.excellent_btn)?.backgroundTintList =
            ContextCompat.getColorStateList(requireContext(), R.color.white)

        // Set the clicked button's background color
        button.backgroundTintList = ContextCompat.getColorStateList(requireContext(), R.color.blue)
    }

}