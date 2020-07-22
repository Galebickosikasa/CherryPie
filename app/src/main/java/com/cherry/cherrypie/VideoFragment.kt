package com.cherry.cherrypie

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView

class VideoFragment : Fragment() {
    private lateinit var container: ViewGroup
    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var recycler: RecyclerView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
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

        val a = (activity as MainActivity).progressMap

        adapter.addItem(SeasonKek(1, a[1]))
        adapter.addItem(SeasonKek(2, a[2]))
        adapter.addItem(SeasonKek(3, a[3]))
        adapter.addItem(SeasonKek(4, a[4]))

    }

}
