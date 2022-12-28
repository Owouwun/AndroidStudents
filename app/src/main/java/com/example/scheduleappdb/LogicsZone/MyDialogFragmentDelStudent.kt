package com.example.scheduleappdb.LogicsZone

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment

class MyDialogFragmentDelStudent: DialogFragment()
{
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog
    {
        val arguments: Bundle? = arguments
        val studentName = arguments?.getString("studentName")
        val builder = AlertDialog.Builder(activity)
        builder.setMessage("Будет удалена зачетная книжка студента $studentName")
            .setTitle("Внимание!")
            .setPositiveButton("Продолжить"
            ) { _, _ -> (activity as MainActivity?)?.delExam() }
            .setNegativeButton("Отмена") { _, _ -> }
        return builder.create()
    }
}