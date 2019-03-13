package com.softwarefactory.retrofittest

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.nostra13.universalimageloader.core.ImageLoader

class RecyclerViewAdapter(private var list:List<Post>,private var imageLoader:ImageLoader):RecyclerView.Adapter<RecyclerViewAdapter.MyHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list,parent,false)
        return MyHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        val posts = list[position]
        val imageUrl = posts.photoUrl
        holder.name.text = posts.title
        holder.color.text = posts.id
        holder.price.text = posts.photoId
        imageLoader.displayImage(imageUrl,holder.image)
    }

    class MyHolder(itemView:View):RecyclerView.ViewHolder(itemView) {
        var name:TextView
        var color:TextView
        var price:TextView
        var image:ImageView
        init {
            name = itemView.findViewById(R.id.name) as TextView
            color = itemView.findViewById(R.id.color) as TextView
            price = itemView.findViewById(R.id.price) as TextView
            image = itemView.findViewById<View>(R.id.productimage) as ImageView
        }
    }

}
