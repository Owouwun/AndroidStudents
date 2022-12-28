package com.example.scheduleappdb.RVZone

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.scheduleappdb.R

class CustomRecyclerAdapterForExams(private val namesE: List<String>,
                                    private val dow: List<Int>,
                                    private val time: List<String>):
    RecyclerView.Adapter<CustomRecyclerAdapterForExams.MyViewHolder>()
{
    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textViewName: TextView = itemView.findViewById(R.id.textViewName)
        val textViewDow: TextView = itemView.findViewById(R.id.textViewRVTime)
        val textViewTime: TextView = itemView.findViewById(R.id.textViewRVDow)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.recyclerview_item, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.textViewName.text = namesE[position]
        holder.textViewDow.text = getDow(dow[position])
        holder.textViewTime.text = time[position]
    }

    override fun getItemCount() = namesE.size

    private fun getDow(dow: Int): String {
        return when (dow) {
            1 -> "понедельник"
            2 -> "вторник"
            3 -> "среда"
            4 -> "четверг"
            5 -> "пятница"
            6 -> "суббота"
            7 -> "воскресенье"
            else -> {"-1"}
        }
    }
}