package com.example.scheduleappdb.LogicsZone

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.scheduleappdb.R
import com.example.scheduleappdb.RVZone.CustomRecyclerAdapterForExams
import com.example.scheduleappdb.UIZone.group.*
import com.google.gson.Gson
import java.text.SimpleDateFormat
import kotlin.collections.ArrayList

class StudentActivity : AppCompatActivity(), ExamDetailsDialogFragment.ExamDetailsDialogListener {
    private lateinit var sa_editText_name: EditText
    private lateinit var sa_editText_number: EditText
    private lateinit var sa_rv_exams: RecyclerView
    private lateinit var sa_textView_mean_value: TextView
    private lateinit var sa_checkBox_confirmed: CheckBox

    private lateinit var curStudent: Student
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.student_activity)

        sa_editText_name = findViewById(R.id.sa_editText_name)
        sa_editText_number = findViewById(R.id.sa_editText_number)
        sa_rv_exams = findViewById(R.id.sa_rv_exams)
        sa_textView_mean_value = findViewById(R.id.sa_textView_mean_value)
        sa_checkBox_confirmed = findViewById(R.id.sa_checkBox_confirmed)

        val action = intent.getIntExtra("action", -1)

        findViewById<Button>(R.id.sa_button_ok).setOnClickListener { confirmChanges(action) }
        findViewById<Button>(R.id.sa_button_addExam).setOnClickListener { addExam() }

        if (action == Constants.Action.Editing.toInt) {
            val gson = Gson()
            curStudent = gson.fromJson(intent.getStringExtra("student"), Student::class.java)
            sa_editText_name.setText(curStudent.name)
            sa_editText_number.setText(curStudent.number.toString())

            sa_rv_exams.adapter = CustomRecyclerAdapterForExams(
                curStudent.getExamNames(),
                curStudent.getExamMarks(),
                curStudent.getExamDates()
            )

            sa_textView_mean_value.text = curStudent.mean.toString()
            sa_checkBox_confirmed.isChecked = curStudent.confirmed

        } else {
            curStudent = Student(
                "",
                -1,
                null,
                -1F,
                false
            )
        }
        if (intent.getStringExtra("examName")!=null)
            sa_textView_mean_value.text = "100"
    }

    private fun confirmChanges(action: Int) {
        if (sa_editText_name.text.toString() != "" && sa_editText_number.text.toString() != "") {
            val intent = Intent(
                this@StudentActivity,
                MainActivity::class.java
            )
            val gson = Gson()

            try {

                intent.putExtra("action", action)
                intent.putExtra(
                    "student", gson.toJson(
                        Student(
                            sa_editText_name.text.toString().trim(),
                            sa_editText_number.text.toString().trim().toInt(),
                            //sa_rv_exams.adapter.toString(),
                            //curStudent.exams,
                            null,
                            if (sa_textView_mean_value.text.toString() == "") -1F
                            else sa_textView_mean_value.text.toString().toFloat(),
                            sa_checkBox_confirmed.isChecked
                        )
                    )
                )

                setResult(RESULT_OK, intent)
                finish()
            } catch (e : java.lang.Exception) {
                val toast = Toast.makeText(
                    applicationContext,
                    "Номер зачетки должен быть числом!",
                    Toast.LENGTH_SHORT
                )
                toast.show()
            }
        } else {
            val toast = Toast.makeText(
                applicationContext,
                "Заполните обязательные поля!",
                Toast.LENGTH_SHORT
            )
            toast.show()
        }
    }

    private fun addExam() {
        val examDetails = ExamDetailsDialogFragment()
        examDetails.show(supportFragmentManager, "ExamDetailsDialogFragment")
    }

    override fun ed_button_confirm_listener(dialog: ExamDetailsDialogFragment) {
        if (dialog.ed_editText_name.text.toString()!="" &&
            dialog.ed_editText_mark.text.toString()!="" &&
            dialog.ed_editText_date.text.toString()!="") {
            //Добавление экзамена в БД нужному студенту
            try {
                dialog.ed_editText_mark.text.toString().toFloat()
            } catch (e: Exception) {
                val toast = Toast.makeText(
                    applicationContext,
                "Оценка должна иметь целочисленное значение!",
                    Toast.LENGTH_SHORT
                )
                toast.show()
                return
            }
            try {
                SimpleDateFormat("dd/mm/yy").parse(dialog.ed_editText_date.text.toString())
            } catch (e: Exception) {
                val toast = Toast.makeText(
                    applicationContext,
                    "Дата должна иметь формат dd/mm/yy!",
                    Toast.LENGTH_SHORT
                )
                toast.show()
                return
            }
            if (curStudent.exams==null) {
                val ALE = ArrayList<Exam>()
                ALE.add(
                    Exam(
                        dialog.ed_editText_name.text.toString(),
                        dialog.ed_editText_mark.text.toString().toInt(),
                        dialog.ed_editText_date.text.toString()
                    )
                )
                curStudent.exams = ALE
            } else {
                curStudent.exams?.add(
                    Exam(
                        dialog.ed_editText_name.text.toString(),
                        dialog.ed_editText_mark.text.toString().toInt(),
                        dialog.ed_editText_date.text.toString()
                    )
                )
            }
            dialog.dismiss()
            sa_rv_exams.adapter!!.notifyDataSetChanged()
        } else {
            val toast = Toast.makeText(
                applicationContext,
                "Заполните обязательные поля!",
                Toast.LENGTH_SHORT
            )
            toast.show()
        }
    }
}