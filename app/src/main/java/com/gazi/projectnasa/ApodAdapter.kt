package com.gazi.projectnasa

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class ApodAdapter(val data : List<ApodObject>) : RecyclerView.Adapter<ApodAdapter.ImageHolder>() {
    class ImageHolder(rowView: View) : RecyclerView.ViewHolder(rowView) {
        val image : ImageView = rowView.findViewById(R.id.apod_image)
        val title : TextView = rowView.findViewById(R.id.apod_item_title)
        val authors : TextView = rowView.findViewById(R.id.apod_title_copyright)
        val explanation : TextView = rowView.findViewById(R.id.apod_item_explanation)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageHolder {
        val viewHolder : ImageHolder
        val rowView : View = LayoutInflater.from(parent.context).inflate(R.layout.list_item_apod, parent, false);
        viewHolder = ImageHolder(rowView);

        return viewHolder
    }

    override fun onBindViewHolder(holder: ImageHolder, position: Int) {
        Glide.with(holder.image.context)
            .load(data[position].url)
            .fitCenter()
            .into(holder.image)

        holder.title.text = data[position].title
        holder.authors.text = data[position].copyright
        holder.explanation.text = data[position].explanation
    }

    override fun getItemCount(): Int {
        return data.size
    }
}