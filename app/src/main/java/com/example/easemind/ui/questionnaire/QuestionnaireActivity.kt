package com.example.easemind.ui.questionnaire

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.ViewSwitcher
import androidx.appcompat.app.AppCompatActivity
import com.example.easemind.R
import com.example.easemind.databinding.ActivityQuestionnaireBinding

class QuestionnaireActivity : AppCompatActivity() {

    private lateinit var binding: ActivityQuestionnaireBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQuestionnaireBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val fragmentManager = supportFragmentManager
        val questionnaireFragment = QuestionBBFragment()
        val fragment = fragmentManager.findFragmentByTag(QuestionBBFragment::class.java.simpleName)

        if (fragment !is QuestionBBFragment) {
            Log.d("MyFragment", "Fragment Name :" + QuestionBBFragment::class.java.simpleName)
            fragmentManager
                .beginTransaction()
                .add(
                    R.id.container,
                    questionnaireFragment,
                    QuestionBBFragment::class.java.simpleName
                )
                .commit()
        }

    }
}

