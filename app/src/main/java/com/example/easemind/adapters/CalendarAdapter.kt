package com.example.easemind.adapters

import DateItem
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.easemind.R

class CalendarAdapter(private val days: List<DateItem>) : RecyclerView.Adapter<CalendarAdapter.DateViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DateViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.date_item, parent, false)
        return DateViewHolder(view)
    }

    override fun onBindViewHolder(holder: DateViewHolder, position: Int) {
        // Get the DateItem at the specified position
        val dateItem = days[position]
        // Set visibility and content based on whether it's a padding item or not
        if (dateItem.isPadding) {
            holder.dateText.visibility = View.INVISIBLE
            holder.dateEmoji.visibility = View.INVISIBLE
        } else {
            holder.dateText.visibility = View.VISIBLE
            holder.dateEmoji.visibility = View.VISIBLE
            // Set the day text and emoji based on the DateItem's sentiment
            holder.dateText.text = dateItem.day.toString()
            holder.dateEmoji.setImageResource(getEmojiResource(dateItem.sentiment))
        }
    }

    override fun getItemCount(): Int = days.size

    // Get the corresponding emoji resource based on the sentiment
    private fun getEmojiResource(sentiment: String): Int {
        return when (sentiment) {
            "happy" -> R.drawable.happy_emoji
            "sad" -> R.drawable.sad_emoji
            "neutral" -> R.drawable.neutral_emoji
            else -> R.drawable.neutral_emoji // Default to neutral emoji if sentiment is unknown
        }
    }

    // ViewHolder for each item in the RecyclerView
    class DateViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var dateText: TextView = itemView.findViewById(R.id.dateText)
        var dateEmoji: ImageView = itemView.findViewById(R.id.dateEmoji)
    }
}
