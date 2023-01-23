package com.example.scheduleappdb.UIZone.group

data class Student(
    val name: String,
    val number: Int,
    var exams: ArrayList<Exam>? = ArrayList(),
    val mean: Float,
    val confirmed: Boolean
) {
    fun getExamNames(): ArrayList<String> {
        val arrayListForReturn: ArrayList<String> = ArrayList()
        if (exams!=null)
            for (i in exams!!)
                arrayListForReturn.add(i.name)
        return arrayListForReturn
    }

    fun getExamMarks(): ArrayList<Int> {
        val arrayListForReturn: ArrayList<Int> = ArrayList()
        if (exams!=null)
            for (i in exams!!)
                arrayListForReturn.add(i.mark)
        return arrayListForReturn
    }

    fun getExamDates(): ArrayList<String> {
        val arrayListForReturn: ArrayList<String> = ArrayList()
        if (exams!=null)
            for (i in exams!!)
                arrayListForReturn.add(i.date)
        return arrayListForReturn
    }
}