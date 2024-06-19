package com.example.easemind.ui.journal

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.easemind.R
import com.example.easemind.data.response.JournalsItem
import com.example.easemind.databinding.ItemJournalBinding

class JournalAdapter : ListAdapter<JournalsItem, JournalAdapter.MyViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemJournalBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val journal = getItem(position)
        holder.bind(journal)
    }

    class MyViewHolder(private val binding: ItemJournalBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(journal: JournalsItem) {
            Glide.with(itemView.context)
                .load(R.drawable.draw_overjoyed)
                .into(binding.ivItemEmoji) //TODO: create categorization
            binding.tvItemDate.text = journal.journalDate
            binding.tvItemCategory.text = journal.result
            binding.tvItemThoughts.text = journal.thoughts

            binding.cardView.setOnClickListener {
                val intent = Intent(itemView.context, DetailJournalActivity::class.java).apply {
                    putExtra(DetailJournalActivity.EXTRA_EMOTION, R.drawable.draw_overjoyed) // TODO utils
                    putExtra(DetailJournalActivity.EXTRA_DATE, journal.journalDate)
                    putExtra(DetailJournalActivity.EXTRA_CATEGORY, journal.result)
                    putExtra(DetailJournalActivity.EXTRA_THOUGHTS, journal.thoughts)
                }
                itemView.context.startActivity(intent)
            }
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<JournalsItem>() {
            override fun areItemsTheSame(oldItem: JournalsItem, newItem: JournalsItem): Boolean {
                return oldItem == newItem
            }
            override fun areContentsTheSame(oldItem: JournalsItem, newItem: JournalsItem): Boolean {
                return oldItem == newItem
            }
        }
    }

}