package com.example.easemind.data.request

data class JournalRequest (
    val checkpoint: Boolean,
    val journalData: JournalData
)

data class JournalData (
    val journalDate: String,
    val faceDetection: String,
    val thoughts: String,
)