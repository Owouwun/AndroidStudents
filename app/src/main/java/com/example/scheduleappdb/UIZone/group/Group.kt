package com.example.scheduleappdb.UIZone.group

data class Group (
    val name: String,
    var listOfSubjects: ArrayList<Subject> = ArrayList()
)
