package com.cherry.cherrypie

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide

class MainAdapter (context : Context): RecyclerView.Adapter<MainAdapter.ViewItemHolder>() {
    var itemList : ArrayList<FoodItem> = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewItemHolder {
        val view = LayoutInflater.from (parent.context).inflate (R.layout.food_item, parent, false)
        return ViewItemHolder (view)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: ViewItemHolder, position: Int) {
        holder.bind (itemList[position])
    }

    fun addItem (item: FoodItem) {
        itemList.add (item)
        notifyItemChanged (itemCount - 1)
    }

    fun clearItems () {
        itemList.clear ()
        notifyDataSetChanged ()
    }

    inner class ViewItemHolder internal constructor (itemView: View): ViewHolder (itemView) {
        var img : ImageView = itemView.findViewById (R.id.food_img)
        var name : TextView = itemView.findViewById (R.id.food_name)

        fun bind (item : FoodItem) {
            name.text = item.name
            Glide.with (itemView.context).load (item.url).into (img)
            img.visibility = View.VISIBLE
        }
    }
}