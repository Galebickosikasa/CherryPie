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
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerFullScreenListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView

class VideoFragment : Fragment(), YouTubePlayerFullScreenListener {
    private lateinit var youTubePlayerView: YouTubePlayerView
    private lateinit var container: ViewGroup
    private lateinit var toolbar: Toolbar
    private lateinit var bottomNavigationView: BottomNavigationView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        this.container = container!!
        return inflater.inflate(R.layout.fragment_video, container, false)
    }

    override fun onStart() {
        super.onStart()
        toolbar = activity!!.findViewById(R.id.main_toolbar)
        bottomNavigationView = activity!!.findViewById(R.id.bottom_nav)
        youTubePlayerView = container.findViewById(R.id.youTubePlayerView)

//        youTubePlayerView.addYouTubePlayerListener (object : AbstractYouTubePlayerListener () {
//            override fun onReady(youTubePlayer: YouTubePlayer) {
//                super.onReady(youTubePlayer)
//                val videoID = "xIBiJ_SzJTA"
//                youTubePlayer.loadVideo (videoID, 0F)
//            }
//        })
//        youTubePlayerView.addFullScreenListener (this)
    }

    override fun onYouTubePlayerEnterFullScreen() { // TODO
        Log.e("kek", "full")
        toolbar.isVisible = false
//        bottomNavigationView.isVisible = false
//        activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
    }

    override fun onYouTubePlayerExitFullScreen() { // TODO
        activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
//        toolbar.isVisible = true  
//        bottomNavigationView.isVisible = true
    }
}
