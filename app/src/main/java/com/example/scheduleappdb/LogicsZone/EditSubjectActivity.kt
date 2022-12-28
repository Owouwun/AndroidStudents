package com.example.scheduleappdb.LogicsZone

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.RadioButton
import android.widget.TextView
import android.widget.Toast
import com.example.scheduleappdb.R
import com.example.scheduleappdb.UIZone.group.Exam
import com.example.scheduleappdb.UIZone.group.Student
import java.util.*
import kotlin.collections.ArrayList

class EditSubjectActivity : AppCompatActivity() {
    private lateinit var aex_name_editText: EditText
    private lateinit var aex_number_editText: EditText
    private lateinit var aex_ex1_editText_name: EditText
    private lateinit var aex_ex1_editText_mark: EditText
    private lateinit var aex_ex1_editText_date: EditText
    private lateinit var aex_ex2_editText_name: EditText
    private lateinit var aex_ex2_editText_mark: EditText
    private lateinit var aex_ex2_editText_date: EditText
    private lateinit var aex_ex3_editText_name: EditText
    private lateinit var aex_ex3_editText_mark: EditText
    private lateinit var aex_ex3_editText_date: EditText
    private lateinit var aex_ex4_editText_name: EditText
    private lateinit var aex_ex4_editText_mark: EditText
    private lateinit var aex_ex4_editText_date: EditText
    private lateinit var aex_ex5_editText_name: EditText
    private lateinit var aex_ex5_editText_mark: EditText
    private lateinit var aex_ex5_editText_date: EditText
    private lateinit var meanMark_value_textView: TextView
    private lateinit var confirmed_checkBox: CheckBox

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_exam)

        aex_name_editText = findViewById(R.id.name_editText)
        aex_number_editText = findViewById(R.id.number_editText)
        aex_ex1_editText_name = findViewById(R.id.ex1_editText_name)
        aex_ex1_editText_mark = findViewById(R.id.ex1_editText_mark)
        aex_ex1_editText_date = findViewById(R.id.ex1_editText_date)
        aex_ex2_editText_name = findViewById(R.id.ex2_editText_name)
        aex_ex2_editText_mark = findViewById(R.id.ex2_editText_mark)
        aex_ex2_editText_date = findViewById(R.id.ex2_editText_date)
        aex_ex3_editText_name = findViewById(R.id.ex3_editText_name)
        aex_ex3_editText_mark = findViewById(R.id.ex3_editText_mark)
        aex_ex3_editText_date = findViewById(R.id.ex3_editText_date)
        aex_ex4_editText_name = findViewById(R.id.ex4_editText_name)
        aex_ex4_editText_mark = findViewById(R.id.ex4_editText_mark)
        aex_ex4_editText_date = findViewById(R.id.ex4_editText_date)
        aex_ex5_editText_name = findViewById(R.id.ex5_editText_name)
        aex_ex5_editText_mark = findViewById(R.id.ex5_editText_mark)
        aex_ex5_editText_date = findViewById(R.id.ex5_editText_date)
        meanMark_value_textView = findViewById(R.id.meanMark_value_textView)
        confirmed_checkBox = findViewById(R.id.confirmed_checkBox)

        val action = intent.getIntExtra("action", -1)

        findViewById<Button>(R.id.button_confirm).setOnClickListener { confirmChanges(action) }

        if (action == Constants.Action.Editing.toInt) {
            val tempStudent = Student(
                intent.getStringExtra("name") ?: "",
                intent.getIntExtra("number",-1),
                intent.getSerializableExtra("exams") as ArrayList<Exam>,
                //data.getSerializableExtra("exams", Class<ArrayList<Exam>>)
                intent.getFloatExtra("mean", -1F),
                intent.getBooleanExtra("confirmed", false)
            )
            aex_name_editText.setText(tempStudent.name)
            aex_number_editText.setText(intent.getIntExtra("number", -1).toString())
            if (tempStudent.exams!=null) {
                var exams_left = tempStudent.exams!!.size
                aex_ex1_editText_name.setText(tempStudent.exams!![0].name)
                aex_ex1_editText_mark.setText(tempStudent.exams!![0].mark.toString())
                aex_ex1_editText_date.setText(tempStudent.exams!![0].date)
                exams_left--
                if (exams_left>0) {
                    aex_ex2_editText_name.setText(tempStudent.exams!![1].name)
                    aex_ex2_editText_mark.setText(tempStudent.exams!![1].mark.toString())
                    aex_ex2_editText_date.setText(tempStudent.exams!![1].date)
                    exams_left--
                    if (exams_left>0) {
                        aex_ex3_editText_name.setText(tempStudent.exams!![2].name)
                        aex_ex3_editText_mark.setText(tempStudent.exams!![2].mark.toString())
                        aex_ex3_editText_date.setText(tempStudent.exams!![2].date)
                        exams_left--
                        if (exams_left>0) {
                            aex_ex4_editText_name.setText(tempStudent.exams!![3].name)
                            aex_ex4_editText_mark.setText(tempStudent.exams!![3].mark.toString())
                            aex_ex4_editText_date.setText(tempStudent.exams!![3].date)
                            exams_left--
                            if (exams_left>0) {
                                aex_ex5_editText_name.setText(tempStudent.exams!![4].name)
                                aex_ex5_editText_mark.setText(tempStudent.exams!![4].mark.toString())
                                aex_ex5_editText_date.setText(tempStudent.exams!![4].date)
                            }
                        }
                    }
                }
            }
            meanMark_value_textView.text = tempStudent.mean.toString()
        }
    }

    private fun confirmChanges(action: Int) {
        if (aex_name_editText.text.toString() != "" && aex_number_editText.text.toString() != "") {
            val intent = Intent(
                this@EditSubjectActivity,
                MainActivity::class.java
            )
            fun putStudent(intent: Intent, student: Student) {
                intent.putExtra("name", student.name)
                intent.putExtra("number", student.number)
                intent.putExtra("exams", student.exams)
                intent.putExtra("mean", student.mean)
                intent.putExtra("confirmed",student.confirmed)
            }
            intent.putExtra("action", action)
            intent.putExtra("name", aex_name_editText.text.toString().trim())
            intent.putExtra("number", aex_number_editText.text.toString().trim())
            var Exams: ArrayList<Exam> = ArrayList()

            Exams.add(
                Exam(
                    aex_ex1_editText_name.text.toString(),
                    aex_ex1_editText_mark.text.toString().toInt(),
                    aex_ex1_editText_date.text.toString()
                )
            )
            intent.putExtra("mean", aex_ex1_editText_mark.text.toString().toInt())

            setResult(RESULT_OK, intent)
            finish()
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