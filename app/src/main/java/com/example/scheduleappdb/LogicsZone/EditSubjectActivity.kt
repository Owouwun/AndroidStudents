package com.example.scheduleappdb.LogicsZone

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.scheduleappdb.R
import java.text.SimpleDateFormat
import java.time.LocalTime
import java.util.*

class EditSubjectActivity : AppCompatActivity() {
    private lateinit var editSubjectName: EditText
    private lateinit var editTeacherName: EditText
    private lateinit var editAuditory: EditText
    private lateinit var editBuilding: EditText
    private lateinit var editTime: EditText
    private lateinit var editDow: EditText
    private lateinit var editWeekParity: EditText
    private lateinit var editReqEq: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_exam)

        editSubjectName = findViewById(R.id.editTextSubjectName)
        editTeacherName = findViewById(R.id.editTextTeacherName)
        editAuditory = findViewById(R.id.editTextAuditory)
        editBuilding = findViewById(R.id.editBuildingDate)
        editTime = findViewById(R.id.editTextTime)
        editDow = findViewById(R.id.editTextDow)
        editWeekParity = findViewById(R.id.editTextWeekParity)
        editReqEq = findViewById(R.id.editTextReqEq)

        val action = intent.getIntExtra("action", -1)

        findViewById<Button>(R.id.button_confirm).setOnClickListener { confirmChanges(action) }

        if (action == Constants.Action.Editing.toInt) {
            editSubjectName.setText(intent.getStringExtra("subject") ?: "")
            editTeacherName.setText(intent.getStringExtra("teacher") ?: "")
            editAuditory.setText(intent.getStringExtra("auditory") ?: "")
            editBuilding.setText(intent.getStringExtra("building") ?: "")
            editTime.setText(intent.getStringExtra("time") ?: "")
            editDow.setText(
                when (intent.getStringExtra("dow") ?: "") {
                    "1" -> "Понедельник"
                    "2" -> "Вторник"
                    "3" -> "Среда"
                    "4" -> "Четверг"
                    "5" -> "Пятница"
                    "6" -> "Суббота"
                    "7" -> "Воскресенье"
                    else -> {
                        "-1"
                    }
                })
            if ((intent.getStringExtra("week_parity") ?: "") == "1")
                editWeekParity.setText("Четная")
            else
                editWeekParity.setText("Нечетная")
            editReqEq.setText(intent.getStringExtra("req_eq") ?: "")
        }
    }

    private fun confirmChanges(action: Int) {
        if (editSubjectName.text.toString() != "" && editTeacherName.text.toString() != ""
            && editAuditory.text.toString() != "" && editBuilding.text.toString() != ""
            && editTime.text.toString() != "" && editDow.text.toString() != ""
            && editWeekParity.text.toString() != "") {

            //if (editWeekParity.text.toString().trim().lowercase(Locale.ROOT) == "четная"
            //    || editWeekParity.text.toString().trim().lowercase(Locale.ROOT) == "нечетная") {
            if (Constants.typesOfWeek.contains(editWeekParity.text.toString().trim().lowercase(Locale.ROOT))) {
                if (isDowValid(editDow.text.toString().trim())
                    && isTimeValid(editTime.text.toString().trim())) {

                    val intent = Intent(
                        this@EditSubjectActivity,
                        MainActivity::class.java
                    )
                    intent.putExtra("action", action)
                    intent.putExtra("subject", editSubjectName.text.toString().trim())
                    intent.putExtra("teacher", editTeacherName.text.toString().trim())
                    intent.putExtra("auditory", editAuditory.text.toString().trim().toInt())
                    intent.putExtra("building", editBuilding.text.toString().trim())
                    intent.putExtra("time", editTime.text.toString().trim())

                    intent.putExtra("dow", when (editDow.text.toString().trim().lowercase(Locale.ROOT)) {
                        "понедельник" -> 1
                        "вторник" -> 2
                        "среда" -> 3
                        "четверг" -> 4
                        "пятница" -> 5
                        "суббота" -> 6
                        "воскресенье"  -> 7
                        else -> {
                            -1
                        }
                    })
                    //if (editWeekParity.text.toString().trim().lowercase(Locale.ROOT) == "четная")
                    if (editWeekParity.text.toString().trim().lowercase(Locale.ROOT) == Constants.typesOfWeek.first().toString())
                        intent.putExtra("week_parity", 1)
                    else
                        intent.putExtra("week_parity", 0)
                    intent.putExtra("req_eq", editReqEq.text.toString().trim())
                    setResult(RESULT_OK, intent)
                    finish()
                } else {
                    val toast = Toast.makeText (
                        applicationContext,
                        "Проверьте день недели и время!",
                        Toast.LENGTH_SHORT
                    )
                    toast.show()
                }
            } else {
                val toast = Toast.makeText (
                    applicationContext,
                    "Поле \"Четность недели\" поддерживает только " +
                            "значения \"четная\" или \"нечетная\"!",
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

    /*
    @SuppressLint("SimpleDateFormat")
    private fun isDateValid(date: String?): Boolean {
        val myFormat = SimpleDateFormat("dd.MM.yyyy")
        myFormat.isLenient = false
        return try {
            if (date != null)
                myFormat.parse(date)
            true
        } catch (e: Exception) {
            false
        }
    }*/

    private fun isDowValid(dow: String?): Boolean {
        return Constants.daysOfWeek.contains(dow?.lowercase(Locale.ROOT))
        /*
        val dow_local = dow?.lowercase(Locale.ROOT)
        return dow_local == "понедельник" ||
                dow_local == "вторник" ||
                dow_local == "среда" ||
                dow_local == "четверг" ||
                dow_local == "пятница" ||
                dow_local == "суббота" ||
                dow_local == "воскресенье"*/
    }

    private fun isTimeValid(date: String?): Boolean {
        return try {
            LocalTime.parse(date)
            true
        } catch (e: java.lang.Exception) {
            false
        }
    }
}