package com.cherry.cherrypie

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder

class SeasonAdapter(context: Context) : RecyclerView.Adapter<ViewHolder>() {
    private val seasonList = arrayListOf<SeasonKek>()
    private val onSeasonClick: OnSeasonClick

    interface OnSeasonClick {
        fun onSeasonClick(num: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.season_container, parent, false)
        return SeasonHolder(view)
    }

    override fun getItemCount(): Int {
        return seasonList.size
    }

    @ExperimentalStdlibApi
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        (holder as SeasonHolder).bind (seasonList[position])
    }

    fun addItem(item: SeasonKek) {
        seasonList.add(item)
        notifyItemChanged(itemCount - 1)
    }

    inner class SeasonHolder internal constructor(itemView: View) : ViewHolder(itemView), View.OnClickListener {
        private val img: ImageView = itemView.findViewById(R.id.season_image)
        private val progress : ProgressBar = itemView.findViewById (R.id.season_progress)

        override fun onClick(v: View?) {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION) onSeasonClick.onSeasonClick(position)
        }

        @ExperimentalStdlibApi
        fun bind(item: SeasonKek) {
            when (item.num) {
                1 -> {
                    img.setImageResource (R.drawable.mr_robot)
                    progress.max = 10
                    progress.progress = item.progress.countOneBits ()
                }
                2 -> {
                    img.setImageResource(R.drawable.season_2)
                    progress.max = 12
                    progress.progress = item.progress.countOneBits ()
                }
                3 -> {
                    img.setImageResource(R.drawable.season_3)
                    progress.max = 10
                    progress.progress = item.progress.countOneBits ()
                }
                4 -> {
                    img.setImageResource(R.drawable.season_4)
                    progress.max = 13
                    progress.progress = item.progress.countOneBits ()
                }
            }
        }

        init {
            itemView.setOnClickListener(this)
        }

    }

    init {
        onSeasonClick = context as OnSeasonClick
    }

}