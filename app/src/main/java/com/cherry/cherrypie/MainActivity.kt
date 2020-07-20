package com.cherry.cherrypie

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.theartofdev.edmodo.cropper.CropImage

class MainActivity : AppCompatActivity(), SeasonAdapter.OnSeasonClick,
    EpisodeAdapter.OnEpisodeClick {
    private lateinit var bottomNav: BottomNavigationView
    private lateinit var cameraFragment: CameraFragment
    private lateinit var videoFragment: VideoFragment
    private lateinit var profileFragment: ProfileFragment
    private lateinit var episodeFragment: EpisodeFragment
    private lateinit var mAuth: FirebaseAuth
    private var user: FirebaseUser? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        bottomNav = findViewById(R.id.bottom_nav)
        cameraFragment = CameraFragment()
        videoFragment = VideoFragment()
        profileFragment = ProfileFragment()
        episodeFragment = EpisodeFragment()
        supportFragmentManager.beginTransaction().replace(R.id.fragment_container, videoFragment)
            .commit()
        mAuth = FirebaseAuth.getInstance()
        user = mAuth.currentUser

        bottomNav.setOnNavigationItemSelectedListener {
            var fragment: Fragment? = null
            when (it.itemId) {
                R.id.video -> {
                    fragment = videoFragment
                }
                R.id.camera -> {
                    startActivity(Intent(this@MainActivity, ArActivity::class.java))
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.profile -> {
                    if (user == null) {
                        val intent = Intent(Constants.LOGIN_ACTIVITY_PATH)
                        startActivityForResult(intent, 1)
                        return@setOnNavigationItemSelectedListener true
                    } else {
                        fragment = profileFragment
                    }
                }
            }
            supportFragmentManager.beginTransaction().replace(R.id.fragment_container, fragment!!)
                .commit()
            return@setOnNavigationItemSelectedListener true
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            1 -> {
                user = mAuth.currentUser
                if (user != null) {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, profileFragment).commit()
                }
            }
            CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE -> {
                profileFragment.photoCrop(resultCode, data)
            }
        }
    }

    fun backFromEpisode() {
        supportFragmentManager.beginTransaction().replace(R.id.fragment_container, videoFragment)
            .commit()
    }

    override fun onSeasonClick(num: Int) {
//        Log.e ("kek", "season $num")
        supportFragmentManager.beginTransaction().replace(R.id.fragment_container, episodeFragment)
            .commit()
        val sp = getSharedPreferences("season", Context.MODE_PRIVATE)
        sp.edit().putInt("season", num + 1).apply()
    }

    override fun onEpisodeClick(num: Int) {
        Log.e("kek", "episode $num")
    }
}
