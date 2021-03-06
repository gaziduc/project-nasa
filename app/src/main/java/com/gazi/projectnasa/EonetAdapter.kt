package com.gazi.projectnasa

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class EonetAdapter(val data: MutableList<EonetData>): RecyclerView.Adapter<EonetAdapter.ViewHolder>() {
    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val nameView : TextView = itemView.findViewById(R.id.activity_eonet_item_txt_name)
        val dateView : TextView = itemView.findViewById(R.id.activity_eonet_item_txt_date)
        val typeView : TextView = itemView.findViewById(R.id.activity_eonet_item_txt_type)
        val magnitudeView : TextView = itemView.findViewById(R.id.activity_eonet_item_txt_magnitude)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val rowView = LayoutInflater.from(parent.context).inflate(R.layout.list_item_eonet, parent, false);
        return ViewHolder(rowView);
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentEvent = data[position];
        holder.nameView.text = currentEvent.name;
        holder.typeView.text = currentEvent.type;
        holder.dateView.text = currentEvent.date;
        if (currentEvent.magnitude != null)
            holder.magnitudeView.text = currentEvent.magnitude.toString() + " km/h";
        else
            holder.magnitudeView.visibility = View.GONE;
    }

    override fun getItemCount(): Int {
        return data.size;
    }
}