package com.example.scheduleappdb.RVZone

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.scheduleappdb.R
import com.example.scheduleappdb.UIZone.group.Exam

class CustomRecyclerAdapterForExams(private var names: ArrayList<String>?,
                                    private var marks: ArrayList<Int>?,
                                    private var dates: ArrayList<String>?
                                    ): RecyclerView.Adapter<CustomRecyclerAdapterForExams.MyViewHolder>() {
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
        if (names!=null) {
            holder.name_textView.text = names!![position]
            holder.mark_textView.text = marks!![position].toString()
            holder.date_textView.text = dates!![position]
        }
    }

    override fun getItemCount() = names?.size ?: 0
}