package com.example.scheduleappdb.UIZone.group

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import java.util.*
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

    fun getSubjectsNames(indexGroup: Int): ArrayList<String> {
        val arrayListForReturn: ArrayList<String> = ArrayList()
        for (i in groups[indexGroup].listOfSubjects)
            arrayListForReturn.add(i.nameOfSubject)
        return arrayListForReturn
    }

    fun getDow(indexGroup: Int): ArrayList<Int> {
        val arrayListForReturn: ArrayList<Int> = ArrayList()
        for (i in groups[indexGroup].listOfSubjects)
            arrayListForReturn.add(i.dow)
        return arrayListForReturn
    }

    fun getTimes(indexGroup: Int) : ArrayList<String> {
        val arrayListForReturn: ArrayList<String> = ArrayList()
        for (i in groups[indexGroup].listOfSubjects)
            arrayListForReturn.add(i.time)
        return arrayListForReturn
    }

    fun getSubject(indexGroup: Int, indexExam: Int): Subject {
        return groups[indexGroup].listOfSubjects[indexExam]
    }

    fun sortSubjects(indexGroup: Int, sortIndex: Int) {
        when (sortIndex) {
            0 -> groups[indexGroup].listOfSubjects.sortBy { it.nameOfSubject }
            1 -> groups[indexGroup].listOfSubjects.sortBy { it.nameOfTeacher }
            2 -> groups[indexGroup].listOfSubjects.sortBy { it.auditory }
            3 -> groups[indexGroup].listOfSubjects.sortBy { it.building }
            4 -> groups[indexGroup].listOfSubjects.sortBy { it.time }
            5 -> groups[indexGroup].listOfSubjects.sortBy { it.dow }
            6 -> groups[indexGroup].listOfSubjects.sortBy { it.weekParity }
            7 -> groups[indexGroup].listOfSubjects.sortBy { it.comment }
        }
    }

/*
        if (sortIndex == 0) { //nameOfSubject
            val tempArrayListOfExamsNames: ArrayList<String> = ArrayList()
            val tempArrayListOfSubjects: ArrayList<Subject> = ArrayList()
            for (i in groups[indexGroup].listOfSubjects)
                tempArrayListOfExamsNames.add(i.nameOfSubject.lowercase(Locale.ROOT))
            tempArrayListOfExamsNames.sort()
            for (i in tempArrayListOfExamsNames)
                for (j in groups[indexGroup].listOfSubjects)
                    if (i == j.nameOfSubject.lowercase(Locale.ROOT) &&
                        !tempArrayListOfSubjects.contains(j)) {
                        tempArrayListOfSubjects.add(j)
                        break
                    }
            groups[indexGroup].listOfSubjects = tempArrayListOfSubjects
        }

        if (sortIndex == 1) { //nameOfTeacher
            val tempArrayListOfTeacherNames: ArrayList<String> = ArrayList()
            val tempArrayListOfSubjects: ArrayList<Subject> = ArrayList()
            for (i in groups[indexGroup].listOfSubjects)
                tempArrayListOfTeacherNames.add(i.nameOfTeacher.lowercase(Locale.ROOT))
            tempArrayListOfTeacherNames.sort()
            for (i in tempArrayListOfTeacherNames)
                for (j in groups[indexGroup].listOfSubjects)
                    if (i == j.nameOfTeacher.lowercase(Locale.ROOT) && !tempArrayListOfSubjects.contains(j)) {
                        tempArrayListOfSubjects.add(j)
                        break
                    }
            groups[indexGroup].listOfSubjects = tempArrayListOfSubjects
        }

        if (sortIndex == 2) { //auditory
            val tempArrayListOfAuditory: ArrayList<Int> = ArrayList()
            val tempArrayListOfSubjects: ArrayList<Subject> = ArrayList()
            for (i in groups[indexGroup].listOfSubjects)
                tempArrayListOfAuditory.add(i.auditory)
            tempArrayListOfAuditory.sort()
            for (i in tempArrayListOfAuditory)
                for (j in groups[indexGroup].listOfSubjects)
                    if (i == j.auditory && !tempArrayListOfSubjects.contains(j)) {
                        tempArrayListOfSubjects.add(j)
                        break
                    }
            groups[indexGroup].listOfSubjects = tempArrayListOfSubjects
        }

        if (sortIndex == 3) { //building
            val tempArrayListOfPeoples: ArrayList<String> = ArrayList()
            val tempArrayListOfSubjects: ArrayList<Subject> = ArrayList()
            for (i in groups[indexGroup].listOfSubjects)
            {
                tempArrayListOfPeoples.add(i.building.lowercase(Locale.ROOT))
            }
            tempArrayListOfPeoples.sort()
            for (i in tempArrayListOfPeoples)
                for (j in groups[indexGroup].listOfSubjects)
                    if (i == j.building.lowercase(Locale.ROOT) && !tempArrayListOfSubjects.contains(j)) {
                        tempArrayListOfSubjects.add(j)
                        break
                    }
            groups[indexGroup].listOfSubjects = tempArrayListOfSubjects
        }

        if (sortIndex == 4) { //time
            val tempArrayListOfPeoples: ArrayList<String> = ArrayList()
            val tempArrayListOfSubjects: ArrayList<Subject> = ArrayList()
            for (i in groups[indexGroup].listOfSubjects)
                tempArrayListOfPeoples.add(i.time.lowercase(Locale.ROOT))
            tempArrayListOfPeoples.sort()
            for (i in tempArrayListOfPeoples)
                for (j in groups[indexGroup].listOfSubjects)
                    if (i == j.time.lowercase(Locale.ROOT) && !tempArrayListOfSubjects.contains(j)) {
                        tempArrayListOfSubjects.add(j)
                        break
                    }
            groups[indexGroup].listOfSubjects = tempArrayListOfSubjects
        }

        if (sortIndex == 5) { //dow
            val tempArrayListOfAuditory: ArrayList<Int> = ArrayList()
            val tempArrayListOfSubjects: ArrayList<Subject> = ArrayList()
            for (i in groups[indexGroup].listOfSubjects)
                tempArrayListOfAuditory.add(i.dow)
            tempArrayListOfAuditory.sort()
            for (i in tempArrayListOfAuditory)
                for (j in groups[indexGroup].listOfSubjects)
                    if (i == j.dow && !tempArrayListOfSubjects.contains(j)) {
                        tempArrayListOfSubjects.add(j)
                        break
                    }
            groups[indexGroup].listOfSubjects = tempArrayListOfSubjects
        }

        if (sortIndex == 6) { //weekParity
            val tempArrayListOfAbstract: ArrayList<Int> = ArrayList()
            val tempArrayListOfSubjects: ArrayList<Subject> = ArrayList()
            for (i in groups[indexGroup].listOfSubjects)
                tempArrayListOfAbstract.add(i.weekParity)
            tempArrayListOfAbstract.sort()
            for (i in tempArrayListOfAbstract)
                for (j in groups[indexGroup].listOfSubjects)
                    if (i == j.weekParity && !tempArrayListOfSubjects.contains(j)) {
                        tempArrayListOfSubjects.add(j)
                        break
                    }
            groups[indexGroup].listOfSubjects = tempArrayListOfSubjects
        }

        if (sortIndex == 7) { //comment
            val tempArrayListOfComment: ArrayList<String> = ArrayList()
            val tempArrayListOfSubjects: ArrayList<Subject> = ArrayList()
            for (i in groups[indexGroup].listOfSubjects)
                tempArrayListOfComment.add(i.comment.lowercase(Locale.ROOT))
            tempArrayListOfComment.sort()
            for (i in tempArrayListOfComment)
                for (j in groups[indexGroup].listOfSubjects)
                    if (i == j.comment.lowercase(Locale.ROOT) && !tempArrayListOfSubjects.contains(j)) {
                        tempArrayListOfSubjects.add(j)
                        break
                    }
            groups[indexGroup].listOfSubjects = tempArrayListOfSubjects
        }
    }
 */
}