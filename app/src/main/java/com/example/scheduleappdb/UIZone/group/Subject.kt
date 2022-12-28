package com.example.scheduleappdb.UIZone.group

data class Subject(
    val nameOfSubject: String,
    val nameOfTeacher: String,
    val auditory: Int,
    val building: String,
    val time: String,
    val dow: Int,
    val weekParity: Int,
    val comment: String
)
