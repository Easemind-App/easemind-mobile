package com.example.easemind

import DateItem
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.easemind.adapters.CalendarAdapter
import java.time.LocalDate

class MonthFragment : Fragment() {

    private var year: Int = 2024
    private var month: Int = 1  // Default to January

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_month, container, false)

        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = GridLayoutManager(context, 7) // 7 for the days of the week

        val currentMonthData = prepareCalendarData(year, month)
        recyclerView.adapter = CalendarAdapter(currentMonthData)

        return view
    }

    // Ini untuk create new instance of MonthFragment
    companion object {
        fun newInstance(year: Int, month: Int): MonthFragment {
            val fragment = MonthFragment()
            fragment.year = year
            fragment.month = month
            return fragment
        }
    }

    // This function prepares calendar data for a given year and month
    @RequiresApi(Build.VERSION_CODES.O)
    private fun prepareCalendarData(year: Int, month: Int): List<DateItem> {
        val days = mutableListOf<DateItem>()
        val monthStart = LocalDate.of(year, month, 1)
        val daysInMonth = monthStart.lengthOfMonth() // Get the number of days in the month
        // Loop through each day in the month
        for (day in 1..daysInMonth) {
            val date = LocalDate.of(year, month, day)
            val sentiment = getSentimentForDate(date) ?: "no_entry" // Get the sentiment for the current day (e.g., happy, no_entry) dan ini masih dummy
            days.add(DateItem(year, month, day, sentiment, false)) // Create a DateItem object for the current day and add it to the list
        }
        return days
    }

    //Function untuk generate data mockup. ini harus diganti dengan function untuk hit API
    private fun getSentimentForDate(date: LocalDate): String? {
        return if (Math.random() > 0.7) "happy" else null
    }
}
