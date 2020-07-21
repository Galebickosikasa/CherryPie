package com.cherry.cherrypie

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import com.google.firebase.database.*
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView
import org.jsoup.Jsoup

class WatchFragment : Fragment(), View.OnClickListener {
    private lateinit var container : ViewGroup
    private lateinit var youtubePlayerView : YouTubePlayerView
    private lateinit var btn : Button
    private lateinit var database : FirebaseDatabase
    private lateinit var databaseReference : DatabaseReference
    private lateinit var description : TextView
    private lateinit var url : String
    private var s = -1
    private var e = -1

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        this.container = container!!
        val callback : OnBackPressedCallback = object : OnBackPressedCallback (true) {
            override fun handleOnBackPressed() {
                (activity as MainActivity).backFromWatch ()
            }
        }
        requireActivity ().onBackPressedDispatcher.addCallback (callback)
        return inflater.inflate (R.layout.fragment_watch, container, false)
    }

    override fun onStart() {
        super.onStart()
        youtubePlayerView = container.findViewById (R.id.youtube_player)
        btn = container.findViewById (R.id.i_need_full)
        description = container.findViewById (R.id.episode_description)
        val sp = activity!!.getSharedPreferences ("video", Context.MODE_PRIVATE)
        val season = sp.getInt ("season", -1)
        val episode = sp.getInt ("episode", -1)
        s = season
        e = episode
        database = FirebaseDatabase.getInstance ()
        databaseReference = database.getReference ("${Constants.VIDEO_PATH}${Constants.SEASONS_PATH}$season/$episode")
        databaseReference.addListenerForSingleValueEvent (object : ValueEventListener {

            override fun onCancelled(error: DatabaseError) {}

            override fun onDataChange(snapshot: DataSnapshot) {
                val item = snapshot.getValue (VideoKek::class.java)
                url = item!!.full_url
                btn.setOnClickListener (this@WatchFragment)

                youtubePlayerView.addYouTubePlayerListener (object : AbstractYouTubePlayerListener () {
                    override fun onReady(youTubePlayer: YouTubePlayer) {
                        super.onReady(youTubePlayer)
                        val videoID = item.trailer
                        youTubePlayer.loadVideo (videoID, 0F)
                    }
                })

                val t = Thread (Runnable {
                    val doc = Jsoup.connect (item.full_url).get ()
                    val text = doc.getElementsByAttributeValue ("itemprop", "description").text ()
                    Log.e ("kek", text)
                    activity!!.runOnUiThread { description.text = text }
                })
                t.start ()
            }
        })

    }

    override fun onClick(v: View?) {
        var num = ((activity as MainActivity).progressMap[s.toString ()]!! as Int)
        num = num or (1 shl e)
        (activity as MainActivity).progressMap[s.toString ()] = num
        (activity as MainActivity).updProgress ()
        val browserIntent = Intent (Intent.ACTION_VIEW, Uri.parse (url))
        startActivity (browserIntent)
    }
}