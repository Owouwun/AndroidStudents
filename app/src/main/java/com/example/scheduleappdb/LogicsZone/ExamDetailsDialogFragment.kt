package com.example.scheduleappdb.LogicsZone

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.example.scheduleappdb.R
import com.example.scheduleappdb.RVZone.CustomRecyclerAdapterForExams
import com.example.scheduleappdb.UIZone.group.Exam
import com.example.scheduleappdb.UIZone.group.Student

class ExamDetailsDialogFragment: DialogFragment() {
    internal lateinit var listener: ExamDetailsDialogListener

    internal lateinit var ed_editText_name: EditText
    internal lateinit var ed_editText_mark: EditText
    internal lateinit var ed_editText_date: EditText
    internal lateinit var ed_button_confirm: Button
    internal lateinit var ed_button_delete: Button

    internal var currentStudentID: Int = -1

    interface ExamDetailsDialogListener {
        fun ed_button_confirm_listener(dialog: ExamDetailsDialogFragment)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            listener = context as ExamDetailsDialogListener
        } catch (e: ClassCastException) {
            throw ClassCastException((context.toString() +
                    " must implement NoticeDialogListener"))
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view: View = inflater.inflate(R.layout.exam_details, container, false)

        ed_editText_name = view.findViewById(R.id.ed_editText_name)
        ed_editText_mark = view.findViewById(R.id.ed_editText_mark)
        ed_editText_date = view.findViewById(R.id.ed_editText_date)
        ed_button_confirm = view.findViewById(R.id.ed_button_confirm)
        ed_button_delete = view.findViewById(R.id.ed_button_delete)

        ed_button_confirm.setOnClickListener{
            listener.ed_button_confirm_listener(this)
        }

        return view
    }
}