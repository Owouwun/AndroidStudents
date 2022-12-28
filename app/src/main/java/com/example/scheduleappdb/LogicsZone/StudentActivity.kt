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
import com.example.scheduleappdb.UIZone.group.Exam
import com.example.scheduleappdb.UIZone.group.Student
import kotlin.collections.ArrayList

class StudentActivity : AppCompatActivity() {
    private lateinit var sa_editText_name: EditText
    private lateinit var sa_editText_number: EditText
    private lateinit var sa_rv_exams: RecyclerView
    private lateinit var sa_textView_mean_value: TextView
    private lateinit var sa_checkBox_confirmed: CheckBox

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

        if (action == Constants.Action.Editing.toInt) {
            val tempStudent = Student(
                intent.getStringExtra("name") ?: "",
                intent.getIntExtra("number",-1),
                intent.getSerializableExtra("exams") as ArrayList<Exam>,
                //data.getSerializableExtra("exams", Class<ArrayList<Exam>>)
                intent.getFloatExtra("mean", -1F),
                intent.getBooleanExtra("confirmed", false)
            )
            sa_editText_name.setText(tempStudent.name)
            sa_editText_number.setText(tempStudent.number.toString())
            sa_rv_exams.adapter = CustomRecyclerAdapterForExams(
                tempStudent.exams
            )
            sa_textView_mean_value.text = tempStudent.mean.toString()
            sa_checkBox_confirmed.isChecked = tempStudent.confirmed
        }
    }

    private fun confirmChanges(action: Int) {
        if (sa_editText_name.text.toString() != "" && sa_editText_number.text.toString() != "") {
            val intent = Intent(
                this@StudentActivity,
                MainActivity::class.java
            )

            intent.putExtra("action", action)
            intent.putExtra("name", sa_editText_name.text.toString().trim())
            intent.putExtra("number", sa_editText_number.text.toString().trim())
            //intent.putExtra("exams", sa_rv_exams.adapter.)
            //intent.putExtra("mean", aex_ex1_editText_mark.text.toString().toInt())
            intent.putExtra("confirmed", sa_checkBox_confirmed.isChecked)

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