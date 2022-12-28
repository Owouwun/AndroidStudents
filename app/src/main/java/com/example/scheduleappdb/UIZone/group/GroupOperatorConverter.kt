package com.example.scheduleappdb.UIZone.group

import androidx.room.TypeConverter

class GroupOperatorConverter
{
    @TypeConverter
    fun fromGO(groups: ArrayList<Group>): String {
        val resultString: StringBuilder = java.lang.StringBuilder()
        for (i in groups) {
            resultString.append("###${i.name}")
            for (j in i.students) {
                resultString.append("##${j.name}#${j.number}")
                if (j.exams!=null)
                    for (k in j.exams!!)
                        resultString.append("#${k.name}?${k.mark}?${k.date}")
                resultString.append("#${j.mean}${j.confirmed}")
            }
        }
        return resultString.toString()
    }

    @TypeConverter
    fun toGO(data: String): ArrayList<Group> {
        val groupsForReturn: ArrayList<Group> = ArrayList()
        val groupsString: List<String> = data.substring(3).split("###")
        for (i in groupsString) {
            val studentsStrings: List<String> = i.substring(2).split("##")
            val tempStudents: ArrayList<Student> = ArrayList()
            for (student in studentsStrings) {
                val partsOfStudentStrings: List<String> = student.split("#")
                val tempExams: ArrayList<Exam> = ArrayList()
                for (k in 2 until partsOfStudentStrings.size-2) {
                    val tempExamProp = partsOfStudentStrings[k].split("?")
                    tempExams.add(
                        Exam(
                            tempExamProp[0],
                            tempExamProp[1].toInt(),
                            tempExamProp[2]
                        )
                    )
                }
                val tempStudent = Student(
                    partsOfStudentStrings[0], partsOfStudentStrings[1].toInt(),
                    tempExams,
                    partsOfStudentStrings[partsOfStudentStrings.size-2].toFloat(), partsOfStudentStrings[partsOfStudentStrings.size-2].toBoolean()
                )
                tempStudents.add(tempStudent)
            }
            groupsForReturn.add(Group(studentsStrings[0], tempStudents))
        }
        return groupsForReturn
    }
}