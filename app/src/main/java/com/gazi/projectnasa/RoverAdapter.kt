package com.gazi.projectnasa

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class RoverAdapter(val data : List<RoverData>) : RecyclerView.Adapter<RoverAdapter.ImageHolder>() {
    class ImageHolder(rowView: View) : RecyclerView.ViewHolder(rowView) {
        val image : ImageView = rowView.findViewById(R.id.list_item_rover_img_rover)
        val name : TextView = rowView.findViewById(R.id.list_item_rover_txt_name)
        val date : TextView = rowView.findViewById(R.id.list_item_rover_txt_date)
        val camera : TextView = rowView.findViewById(R.id.list_item_rover_txt_camera)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageHolder {
        val viewHolder : ImageHolder
        val rowView : View = LayoutInflater.from(parent.context).inflate(R.layout.list_item_rover, parent, false);
        viewHolder = ImageHolder(rowView);

        return viewHolder
    }

    override fun onBindViewHolder(holder: ImageHolder, position: Int) {
        Glide.with(holder.image.context)
            .load(data[position].img)
            .fitCenter()
            .into(holder.image)

        holder.name.text = data[position].name
        holder.date.text = data[position].date
        holder.camera.text = data[position].camera
    }

    override fun getItemCount(): Int {
        return data.size
    }
}