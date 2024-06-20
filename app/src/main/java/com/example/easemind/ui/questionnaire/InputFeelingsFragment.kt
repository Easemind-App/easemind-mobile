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

class InputFeelingsFragment : Fragment(), View.OnClickListener {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_input_feelings, container, false)

        val arrowBack = view.findViewById<ImageView>(R.id.arrow_back)
        arrowBack.setOnClickListener {
            // Handle the back navigation
            parentFragmentManager.popBackStack()
        }
        return view
    }

    override fun onClick(v: View?) {
        if (v != null) {
            if (v.id == R.id.analyze) {
                Log.d("ResultFragment", "Analyze button clicked")
                val resultFragment = ResultFragment()
                val fragmentManager = parentFragmentManager
                fragmentManager.beginTransaction().apply {
                    replace(R.id.container, resultFragment, ResultFragment::class.java.simpleName)
                    addToBackStack(null)
                    commit()
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val btnAnalyze: Button = view.findViewById(R.id.analyze)
        btnAnalyze.setOnClickListener(this)
    }


}