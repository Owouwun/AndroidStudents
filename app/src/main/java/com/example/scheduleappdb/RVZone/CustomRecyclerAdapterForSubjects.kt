package com.example.scheduleappdb.RVZone

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.scheduleappdb.R

class CustomRecyclerAdapterForStudents(private val names: List<String>,
                                       private val numbers: List<Int>,
                                       private val means: List<Float>):
    RecyclerView.Adapter<CustomRecyclerAdapterForStudents.MyViewHolder>()
{
    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name_textView: TextView = itemView.findViewById(R.id.rvitem_name_textView)
        val number_textView: TextView = itemView.findViewById(R.id.rvitem_number_textView_value)
        val mean_textView: TextView = itemView.findViewById(R.id.rvitem_mean_textView_value)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.recyclerview_item, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.name_textView.text = names[position]
        holder.mean_textView.text = means[position].toString()
        holder.number_textView.text = numbers[position].toString()
    }

    override fun getItemCount() = names.size
}