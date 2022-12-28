package com.example.scheduleappdb.UIZone.group

data class Student(
    val name: String,
    val number: Int,
    var exams: ArrayList<Exam> = ArrayList(),
    val mean: Float,
    val confirmed: Boolean
)
