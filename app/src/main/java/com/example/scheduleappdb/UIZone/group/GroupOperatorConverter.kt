package com.example.scheduleappdb.UIZone.group

import androidx.room.TypeConverter

class GroupOperatorConverter
{
    @JvmName("fromGroups")
    @TypeConverter
    fun fromGO(groups: ArrayList<Group>): String {
        val resultString: StringBuilder = java.lang.StringBuilder()
        for (i in groups) {
            resultString.append("###${i.name}")
            for (j in i.students) {
                /*resultString.append("##${j.name}#${j.number}")
                if (j.exams!=null)
                    for (k in j.exams!!)
                        resultString.append("#${k.name}?${k.mark}?${k.date}")
                resultString.append("#${j.mean}${j.confirmed}")*/
                resultString.append(fromGO(j))
            }
        }
        return resultString.toString()
    }

    @JvmName("fromStudent")
    @TypeConverter
    fun fromGO(student: Student): String {
        val resultString: StringBuilder = java.lang.StringBuilder()
        resultString.append("##${student.name}#${student.number}")
        if (student.exams != null)
            fromGO(student.exams!!)
            //for (i in student.exams!!)
            //    //resultString.append("#${i.name}?${i.mark}?${i.date}")
            //    resultString.append(fromGO(i))
        resultString.append("#${student.mean}#${student.confirmed}")
        return resultString.toString()
    }

    @JvmName("fromExams")
    @TypeConverter
    fun fromGO(exams: ArrayList<Exam>) : String {
        val resultString: StringBuilder = java.lang.StringBuilder()
        for (exam in exams)
            resultString.append(fromGO(exam))
        return resultString.toString()
    }

    @JvmName("fromExam")
    @TypeConverter
    fun fromGO(exam: Exam): String {
        val resultString: StringBuilder = java.lang.StringBuilder()
        resultString.append("#${exam.name}?${exam.mark}?${exam.date}")
        return resultString.toString()
    }

    @JvmName("toGroups")
    @TypeConverter
    fun toGO(data: String): ArrayList<Group> {
        val groupsForReturn: ArrayList<Group> = ArrayList()
        val groupsString: List<String> = data.substring(3).split("###")
        for (i in groupsString) {
            val studentsStrings: List<String> = i.split("##").drop(0)
            val tempStudents: ArrayList<Student> = ArrayList()
            for (student in studentsStrings.subList(1,studentsStrings.size)) {
                /*val partsOfStudentStrings: List<String> = student.split("#")
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
                */
                tempStudents.add(
                    toStudent(student)
                )
            }
            groupsForReturn.add(Group(studentsStrings[0], tempStudents))
        }
        return groupsForReturn
    }

    @JvmName("toStudent")
    @TypeConverter
    fun toStudent(studentString: String): Student {
        val partsOfStudentStrings: List<String> = studentString.split("#")
        val tempExams: ArrayList<Exam> = ArrayList()
        for (k in 2 until partsOfStudentStrings.size-2) {
            tempExams.add(
                toExam(partsOfStudentStrings[k])
            )
        }
        val tempStudent = Student(
            partsOfStudentStrings[0], partsOfStudentStrings[1].toInt(),
            tempExams,
            partsOfStudentStrings[partsOfStudentStrings.size-2].toFloat(), partsOfStudentStrings[partsOfStudentStrings.size-2].toBoolean()
        )
        return tempStudent
    }

    @JvmName("toExam")
    @TypeConverter
    fun toExam(data: String): Exam {
        val tempExamProp = data.split("?")
        return Exam(
            tempExamProp[0],
            tempExamProp[1].toInt(),
            tempExamProp[2]
        )
    }
}