package com.example.scheduleappdb.LogicsZone

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.core.content.getSystemService
import com.example.scheduleappdb.R

class SubjectDetailsDialogFragment: androidx.fragment.app.DialogFragment()
{
    private val exceptionTag = "ExamDetailsDialogFragment"

    interface OnInputListenerSortId {
        fun sendInputSortId(sortId: Int)
    }

    lateinit var onInputListenerSortId: OnInputListenerSortId

    private lateinit var textViewSubjectName: TextView
    private lateinit var textViewTeacherName: TextView
    private lateinit var textViewAuditory: TextView
    private lateinit var textViewBuilding: TextView
    private lateinit var textViewTime: TextView
    private lateinit var textViewDow: TextView
    private lateinit var textViewWeekParity: TextView
    private lateinit var textViewReqEq: TextView
    private lateinit var buttonDel: Button
    private lateinit var buttonEdit: Button
    private lateinit var buttonOk: Button

    private var currentIdForSort: Int = -1

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view: View = inflater.inflate(R.layout.exam_details, container, false)
        textViewSubjectName = view.findViewById(R.id.textViewSubjectName)
        textViewTeacherName = view.findViewById(R.id.textViewTeacherName)
        textViewAuditory = view.findViewById(R.id.textViewAuditory)
        textViewBuilding = view.findViewById(R.id.textViewBuilding)
        textViewTime = view.findViewById(R.id.textViewTime)
        textViewDow = view.findViewById(R.id.textViewDow)
        textViewWeekParity = view.findViewById(R.id.textViewWeekParity)
        textViewReqEq = view.findViewById(R.id.textViewReqEq)
        buttonDel = view.findViewById(R.id.button_details_delete)
        buttonEdit = view.findViewById(R.id.button_details_edit)
        buttonOk = view.findViewById(R.id.button_details_ok)

        textViewSubjectName.setOnLongClickListener { setSortId(0) }
        textViewTeacherName.setOnLongClickListener { setSortId(1) }
        textViewAuditory.setOnLongClickListener { setSortId(2) }
        textViewBuilding.setOnLongClickListener { setSortId(3) }
        textViewTime.setOnLongClickListener { setSortId(4) }
        textViewDow.setOnLongClickListener { setSortId(5) }
        textViewWeekParity.setOnLongClickListener { setSortId(6) }
        textViewReqEq.setOnLongClickListener { setSortId(7) }

        buttonDel.setOnClickListener { returnDel() }
        buttonEdit.setOnClickListener { returnEdit() }
        buttonOk.setOnClickListener { returnIdForSort() }

        val arguments: Bundle? = arguments
        textViewSubjectName.text = arguments!!.getString("subjectName")
        textViewTeacherName.text = arguments.getString("teacherName")
        textViewAuditory.text = arguments.getString("auditory")
        textViewBuilding.text = arguments.getString("building")
        textViewTime.text = arguments.getString("time")
        textViewDow.text = when (arguments.getString("dow")) {
            "1" -> "понедельник"
            "2" -> "вторник"
            "3" -> "среда"
            "4" -> "четверг"
            "5" -> "пятница"
            "6" -> "суббота"
            "7" -> "воскресенье"
            else -> {
                "-1"
            }
        }
        if (arguments.getString("week_parity") == "1")
            textViewWeekParity.text = "четная"
        else
            textViewWeekParity.text = "нечетная"
        textViewReqEq.text = arguments.getString("req_eq")
        if (arguments.getString("connection") != "1") {
            buttonDel.isEnabled = false
            buttonEdit.isEnabled = false
        }

        return view
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            onInputListenerSortId = activity as OnInputListenerSortId
        } catch (e: ClassCastException) {
            Log.e(exceptionTag, "onAttach: ClassCastException: " + e.message)
        }
    }

    private fun setSortId(id: Int): Boolean {
        val vibrator = context?.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        vibrator.vibrate(
            VibrationEffect.createOneShot(
                200,
                VibrationEffect.DEFAULT_AMPLITUDE
            )
        )
        return true
    }

    private fun returnIdForSort() {
        onInputListenerSortId.sendInputSortId(currentIdForSort)
        dialog?.dismiss()
    }

    private fun returnDel() {
        currentIdForSort = 8
        returnIdForSort()
    }

    private fun returnEdit() {
        currentIdForSort = 9
        returnIdForSort()
    }
}