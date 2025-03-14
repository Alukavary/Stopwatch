package com.example.stopwatch

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ItemAdapter(var items: List<ItemDataClass>): RecyclerView.Adapter<ItemAdapter.ViewHolder>() {
    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val circleTime: TextView = view.findViewById(R.id.number_circle )
        val timeText: TextView = view.findViewById(R.id.text_time)

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
           val view = LayoutInflater.from(parent.context).inflate(R.layout.item, parent, false)
            return ViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {
        val item = items[position]
        holder.circleTime.text = item.numberCircle
        holder.timeText.text = item.time
    }

    override fun getItemCount(): Int {
      return items.size
    }

    fun updateData(newData: List<ItemDataClass>){
        items = newData
        notifyDataSetChanged()
    }



}