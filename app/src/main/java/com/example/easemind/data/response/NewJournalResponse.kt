package com.example.easemind.data.response

import com.google.gson.annotations.SerializedName

data class NewJournalResponse(

	@field:SerializedName("journals")
	val journals: Journals? = null
)

data class Journals(

	@field:SerializedName("checkpoint")
	val checkpoint: Boolean? = null,

	@field:SerializedName("journals")
	val journals: List<JournalsItem?>? = null
)
