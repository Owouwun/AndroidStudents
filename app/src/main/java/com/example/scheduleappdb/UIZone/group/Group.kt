package com.example.scheduleappdb.UIZone.group

data class Group (
    val name: String,
    var students: ArrayList<Student> = ArrayList()
)
