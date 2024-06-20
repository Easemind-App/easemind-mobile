package com.example.easemind.data.response

import com.google.gson.annotations.SerializedName

data class JournalResponse(

	@field:SerializedName("journals")
	val journals: List<JournalsItem>,

	@field:SerializedName("message")
	val message: String,
)

data class JournalsItem(

	@field:SerializedName("result")
	val result: String,

	@field:SerializedName("thoughts")
	val thoughts: String,

	@field:SerializedName("quote")
	val quote: String,

	@field:SerializedName("thoughtSentiment")
	val thoughtSentiment: String,

	@field:SerializedName("journalDate")
	val journalDate: String,

	@field:SerializedName("id")
	val id: String,

	@field:SerializedName("isActive")
	val isActive: Boolean
)
