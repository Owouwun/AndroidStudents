package com.example.scheduleappdb.UIZone.group

import androidx.room.TypeConverter

class GroupOperatorConverter
{
    @TypeConverter
    fun fromGO(groups: ArrayList<Group>): String
    {
        val resultString: StringBuilder = java.lang.StringBuilder()
        for (i in groups) {
            resultString.append("###${i.name}")
            for (j in i.listOfSubjects)
                resultString.append(
                    "##${j.nameOfSubject}" +
                    "#${j.nameOfTeacher}#${j.auditory}#${j.building}#${j.time}" +
                        "#${j.dow}#${j.weekParity}#${j.comment}"
                )
        }
        return resultString.toString()
    }

    @TypeConverter
    fun toGO(data: String): ArrayList<Group> {
        val groupsForReturn: ArrayList<Group> = ArrayList()
        val groupsString: List<String> = data.substring(3).split("###")
        for (i in groupsString) {
            val partsOfGroup: List<String> = i.split("##")
            val tempSubjects: ArrayList<Subject> = ArrayList()
            for (j in 1 until partsOfGroup.size) {
                val partsOfSubject: List<String> = partsOfGroup[j].split("#")
                val tempExam = Subject(
                    partsOfSubject[0], partsOfSubject[1], partsOfSubject[2].toInt(),
                    partsOfSubject[3], partsOfSubject[4], partsOfSubject[5].toInt(),
                    partsOfSubject[6].toInt(), partsOfSubject[7]
                )
                tempSubjects.add(tempExam)
            }
            groupsForReturn.add(Group(partsOfGroup[0], tempSubjects))
        }
        return groupsForReturn
    }
}