package com.example.scheduleappdb.UIZone.group

data class Group (
    val name: String,
    var listOfStudents: ArrayList<Student> = ArrayList()
)
