package com.cherry.cherrypie

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder

class EpisodeAdapter(context: Context) : RecyclerView.Adapter<ViewHolder>() {
    private val episodeList = ArrayList<EpisodeKek>()
    private var onEpisodeClick: OnEpisodeClick

    interface OnEpisodeClick {
        fun onEpisodeClick(num: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.episode_container, parent, false)
        return EpisodeHolder(view)
    }

    override fun getItemCount(): Int {
        return episodeList.size
    }

    fun clearItems() {
        episodeList.clear()
        notifyDataSetChanged()
    }

    fun addItem(item: EpisodeKek) {
        episodeList.add(item)
        notifyItemChanged(itemCount - 1)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        (holder as EpisodeHolder).bind(episodeList[position])
    }

    inner class EpisodeHolder internal constructor(itemView: View) : ViewHolder(itemView), View.OnClickListener {
        private val tv: TextView = itemView.findViewById (R.id.episode_title)
        private val used : ImageView = itemView.findViewById (R.id.episode_used)

        override fun onClick(v: View?) {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION) onEpisodeClick.onEpisodeClick (position)
        }

        fun bind(item: EpisodeKek) {
            tv.text = "${item.num} серия"
            used.isVisible = item.used
        }

        init {
            itemView.setOnClickListener (this)
        }
    }

    init {
        onEpisodeClick = context as OnEpisodeClick
    }
}