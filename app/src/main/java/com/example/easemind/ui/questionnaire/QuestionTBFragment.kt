package com.example.easemind.ui.questionnaire

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import com.example.easemind.R

class QuestionTBFragment : Fragment(), View.OnClickListener {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_question_t_b, container, false)

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

    override fun onClick(v: View) {
        if (v.id == R.id.next) {
            Log.d("QuestionTBFragment", "Next button clicked")
            val questionSleepPatternFragment = QuestionSleepPatternsFragment()
            val fragmentManager = parentFragmentManager
            fragmentManager.beginTransaction().apply {
                replace(R.id.container, questionSleepPatternFragment, QuestionSleepPatternsFragment::class.java.simpleName)
                addToBackStack(null)
                commit()
            }
        }
    }
}