package com.cherry.cherrypie

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


class EpisodeFragment : Fragment() {
    private lateinit var recycler: RecyclerView
    private lateinit var container: ViewGroup
    private lateinit var adapter: EpisodeAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        this.container = container!!
        val callback: OnBackPressedCallback = object : OnBackPressedCallback (true) {
            override fun handleOnBackPressed () {
                (activity as MainActivity).backFromEpisode ()
            }
        }
        requireActivity ().onBackPressedDispatcher.addCallback (this, callback)
        return inflater.inflate (R.layout.fragment_episode, container, false)
    }

    override fun onStart() {
        super.onStart()
        recycler = container.findViewById(R.id.episode_recycler)
        recycler.layoutManager = LinearLayoutManager(activity!!)
        adapter = EpisodeAdapter(activity!!)
        recycler.adapter = adapter
        val sp = activity!!.getSharedPreferences("video", Context.MODE_PRIVATE)
        val season = sp.getInt("season", -1)
//        Log.e ("kek", "season from fragment $season")
        adapter.clearItems()
        when (season) {
            1 -> {
                for (i in 1..10) {
                    adapter.addItem(EpisodeKek(i))
                }
            }
            2 -> {
                for (i in 1..12) {
                    adapter.addItem(EpisodeKek(i))
                }
            }
            3 -> {
                for (i in 1..10) {
                    adapter.addItem(EpisodeKek(i))
                }
            }
            4 -> {
                for (i in 1..13) {
                    adapter.addItem(EpisodeKek(i))
                }
            }
        }
    }


}