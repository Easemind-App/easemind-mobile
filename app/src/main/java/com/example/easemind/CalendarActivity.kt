package com.example.easemind

import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2


class CalendarActivity : AppCompatActivity() {


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calendar)

        val viewPager = findViewById<ViewPager2>(R.id.viewPager)
        viewPager.adapter = object : FragmentStateAdapter(this) {
            override fun getItemCount(): Int = 12  // 12 months in a year

            // Create and return a new instance of MonthFragment for the given position
            override fun createFragment(position: Int): Fragment {
                // Create a new instance of MonthFragment for the specified year (2024) and month (position + 1)
                // (position + 1) because position starts from 0 but month numbering starts from 1
                return MonthFragment.newInstance(2024, position + 1)
            }
        }
    }
}
