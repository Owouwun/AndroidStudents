package com.example.scheduleappdb.UIZone.group

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import kotlin.collections.ArrayList

@Entity
class GroupOperator()
{
    @PrimaryKey
    private var id: Int = 1

    @TypeConverters(GroupOperatorConverter::class)
    private var groups: ArrayList<Group> = ArrayList()

    fun getGroups(): ArrayList<Group> {
        return groups
    }

    fun setGroups(newGroups: ArrayList<Group>) {
        groups = newGroups
    }

    fun setId(id: Int) {
        this.id = id
    }

    fun getId(): Int {
        return id
    }

    fun getNames(indexGroup: Int): ArrayList<String> {
        val arrayListForReturn: ArrayList<String> = ArrayList()
        for (i in groups[indexGroup].listOfStudents)
            arrayListForReturn.add(i.name)
        return arrayListForReturn
    }

    fun getNumbers(indexGroup: Int): ArrayList<Int> {
        val arrayListForReturn: ArrayList<Int> = ArrayList()
        for (i in groups[indexGroup].listOfStudents)
            arrayListForReturn.add(i.number)
        return arrayListForReturn
    }

    fun getMeans(indexGroup: Int) : ArrayList<Float> {
        val arrayListForReturn: ArrayList<Float> = ArrayList()
        for (i in groups[indexGroup].listOfStudents)
            arrayListForReturn.add(i.mean)
        return arrayListForReturn
    }

    fun getStudent(indexGroup: Int, indexExam: Int): Student {
        return groups[indexGroup].listOfStudents[indexExam]
    }

    fun sortStudents(indexGroup: Int, sortIndex: Int) {
        when (sortIndex) {
            0 -> groups[indexGroup].listOfStudents.sortBy { it.name }
            1 -> groups[indexGroup].listOfStudents.sortBy { it.number }
            2 -> groups[indexGroup].listOfStudents.sortBy { it.confirmed }
        }
    }
}