package com.cherry.cherrypie

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerFullScreenListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView

class VideoFragment : Fragment() {
    private lateinit var container: ViewGroup
    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var recycler: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        this.container = container!!
        return inflater.inflate(R.layout.fragment_video, container, false)
    }

    override fun onStart() {
        super.onStart()
        bottomNavigationView = activity!!.findViewById(R.id.bottom_nav)
        recycler = container.findViewById(R.id.season_recycler)
        recycler.layoutManager = LinearLayoutManager(activity)
        val adapter = SeasonAdapter(activity!!)
        recycler.adapter = adapter

        adapter.addItem(SeasonKek(1))
        adapter.addItem(SeasonKek(2))
        adapter.addItem(SeasonKek(3))
        adapter.addItem(SeasonKek(4))

    }

}
