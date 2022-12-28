package com.example.scheduleappdb.RVZone

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.scheduleappdb.R
import com.example.scheduleappdb.UIZone.group.Exam

class CustomRecyclerAdapterForExams(private val exams: ArrayList<Exam>?):
    RecyclerView.Adapter<CustomRecyclerAdapterForExams.MyViewHolder>() {

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name_textView: TextView = itemView.findViewById(R.id.rvie_textView_name)
        val mark_textView: TextView = itemView.findViewById(R.id.rvie_textView_mark)
        val date_textView: TextView = itemView.findViewById(R.id.rvie_textView_date)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.recyclerview_item_exam, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        if (exams!=null) {
            holder.name_textView.text = exams[position].name
            holder.mark_textView.text = exams[position].mark.toString()
            holder.date_textView.text = exams[position].date
        }
    }

    override fun getItemCount() = exams?.size ?: 0
}