package com.example.scheduleappdb.UIZone.group

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import kotlin.collections.ArrayList

@Entity
class GroupOperator
{
    @PrimaryKey
    private var id: Int = 1

    @TypeConverters(GroupOperatorConverter::class)
    private var groups: ArrayList<Group>? = ArrayList()

    fun getGroups(): ArrayList<Group>? {
        return groups
    }

    fun setGroups(newGroups: ArrayList<Group>?) {
        groups = newGroups
    }

    fun setId(id: Int) {
        this.id = id
    }

    fun getId(): Int {
        return id
    }

    fun getStudentNames(indexGroup: Int): ArrayList<String> {
        val arrayListForReturn: ArrayList<String> = ArrayList()
        if (groups!=null)
            for (i in groups!![indexGroup].students)
                arrayListForReturn.add(i.name)
        return arrayListForReturn
    }

    fun getStudentNumbers(indexGroup: Int): ArrayList<Int> {
        val arrayListForReturn: ArrayList<Int> = ArrayList()
        if (groups!=null)
            for (i in groups!![indexGroup].students)
                arrayListForReturn.add(i.number)
        return arrayListForReturn
    }

    fun getStudentMeans(indexGroup: Int) : ArrayList<Float> {
        val arrayListForReturn: ArrayList<Float> = ArrayList()
        if (groups!=null)
            for (i in groups!![indexGroup].students)
                arrayListForReturn.add(i.mean)
        return arrayListForReturn
    }

    fun getStudent(indexGroup: Int, indexStudent: Int): Student {
        return groups!![indexGroup].students[indexStudent]
    }

    fun sortStudents(indexGroup: Int, sortIndex: Int) {
        when (sortIndex) {
            0 -> groups!![indexGroup].students.sortBy { it.name }
            1 -> groups!![indexGroup].students.sortBy { it.number }
            2 -> groups!![indexGroup].students.sortBy { it.confirmed }
        }
    }
}