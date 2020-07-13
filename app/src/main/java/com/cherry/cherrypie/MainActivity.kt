package com.cherry.cherrypie

import androidx.fragment.app.Fragment
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    private lateinit var bottomNav : BottomNavigationView
    private lateinit var cameraFragment : CameraFragment
    private lateinit var videoFragment: VideoFragment
    private lateinit var profileFragment: ProfileFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        bottomNav = findViewById (R.id.bottom_nav)
        cameraFragment = CameraFragment ()
        videoFragment = VideoFragment ()
        profileFragment = ProfileFragment ()
        supportFragmentManager.beginTransaction ().replace (R.id.fragment_container, videoFragment).commit ()

        bottomNav.setOnNavigationItemSelectedListener {
                var fragment: Fragment? = null
                when (it.itemId) {
                    R.id.video -> {
                        fragment = videoFragment
                    }
                    R.id.camera -> {
                        fragment = cameraFragment
                    }
                    R.id.profile -> {
                        fragment = profileFragment
                    }
                }
                supportFragmentManager.beginTransaction ().replace (R.id.fragment_container, fragment!!).commit ()
            return@setOnNavigationItemSelectedListener true
        }
    }


}