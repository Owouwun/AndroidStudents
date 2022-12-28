package com.example.scheduleappdb.LogicsZone

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
import androidx.recyclerview.widget.RecyclerView
import com.example.scheduleappdb.R
import com.example.scheduleappdb.RVZone.CustomRecyclerAdapterForExams
import com.example.scheduleappdb.RVZone.CustomRecyclerAdapterForStudents
import com.example.scheduleappdb.UIZone.group.Exam

class StudentDetailsDialogFragment: androidx.fragment.app.DialogFragment()
{
    private val exceptionTag = "ExamDetailsDialogFragment"

    interface OnInputListenerSortId {
        fun sendInputSortId(sortId: Int)
    }

    lateinit var onInputListenerSortId: OnInputListenerSortId

    private lateinit var sd_textView_name_value: TextView
    private lateinit var sd_textView_number_value: TextView
    private lateinit var sd_rv_exams: RecyclerView
    private lateinit var sd_textView_mean_value: TextView
    private lateinit var sd_textView_confirmed_value: TextView
    private lateinit var sd_button_delete: Button
    private lateinit var sd_button_edit: Button
    private lateinit var sd_button_ok: Button

    private var currentIdForSort: Int = -1

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view: View = inflater.inflate(R.layout.student_details, container, false)
        sd_textView_name_value = view.findViewById(R.id.sd_textView_name_value)
        sd_textView_number_value = view.findViewById(R.id.sd_textView_number_value)
        sd_rv_exams = view.findViewById(R.id.sd_rv_exams)
        sd_textView_mean_value = view.findViewById(R.id.sd_textView_mean_value)
        sd_textView_confirmed_value = view.findViewById(R.id.sd_textView_confirmed_value)
        sd_button_delete = view.findViewById(R.id.sd_button_delete)
        sd_button_edit = view.findViewById(R.id.sd_button_edit)
        sd_button_ok = view.findViewById(R.id.sd_button_ok)

        sd_textView_name_value.setOnLongClickListener { setSortId(0) }
        sd_textView_number_value.setOnLongClickListener { setSortId(1) }
        sd_textView_mean_value.setOnLongClickListener { setSortId(2) }
        sd_textView_confirmed_value.setOnLongClickListener { setSortId(3) }

        sd_button_delete.setOnClickListener { returnDel() }
        sd_button_edit.setOnClickListener { returnEdit() }
        sd_button_ok.setOnClickListener { returnIdForSort() }

        val arguments: Bundle? = arguments
        sd_textView_name_value.text = arguments!!.getString("studentName") ?: ""
        sd_textView_number_value.text = arguments.getInt("number", -1).toString()
        sd_rv_exams.adapter = CustomRecyclerAdapterForExams(
            arguments.getSerializable("exams") as ArrayList<Exam>
        )
        sd_textView_mean_value.text = arguments.getString("mean") ?: ""
        sd_textView_confirmed_value

        if (arguments.getString("connection") != Constants.ConnectionStage.SuccessfulConnection.toInt.toString()) {
            sd_button_delete.isEnabled = false
            sd_button_edit.isEnabled = false
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