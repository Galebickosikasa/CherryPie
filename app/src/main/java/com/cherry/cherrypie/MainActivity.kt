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
import com.google.firebase.database.*
import com.theartofdev.edmodo.cropper.CropImage

class MainActivity : AppCompatActivity(), SeasonAdapter.OnSeasonClick, EpisodeAdapter.OnEpisodeClick {
    private lateinit var bottomNav : BottomNavigationView
    private lateinit var cameraFragment : CameraFragment
    private lateinit var videoFragment : VideoFragment
    private lateinit var profileFragment : ProfileFragment
    private lateinit var episodeFragment : EpisodeFragment
    private lateinit var watchFragment : WatchFragment
    private lateinit var mAuth : FirebaseAuth
    private lateinit var database : FirebaseDatabase
    private lateinit var databaseReference : DatabaseReference
    private var user : FirebaseUser? = null
    var progressMap : HashMap<String, Any> = hashMapOf ()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate (savedInstanceState)
        setContentView (R.layout.activity_main)
        bottomNav = findViewById (R.id.bottom_nav)
        cameraFragment = CameraFragment ()
        videoFragment = VideoFragment ()
        profileFragment = ProfileFragment ()
        episodeFragment = EpisodeFragment ()
        watchFragment = WatchFragment ()
        database = FirebaseDatabase.getInstance ()
        supportFragmentManager.beginTransaction ().replace (R.id.fragment_container, videoFragment).commit ()
        mAuth = FirebaseAuth.getInstance ()
        user = mAuth.currentUser
        if (user != null) syncProgress ()

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
                        val intent = Intent (Constants.LOGIN_ACTIVITY_PATH)
                        startActivityForResult (intent, 1)
                        return@setOnNavigationItemSelectedListener true
                    } else {
                        fragment = profileFragment
                    }
                }
            }
            supportFragmentManager.beginTransaction ().replace (R.id.fragment_container, fragment!!).commit ()
            return@setOnNavigationItemSelectedListener true
        }
    }

    private fun syncProgress () {
        databaseReference = database.getReference (Constants.VIDEO_PATH + Constants.PROGRESS_PATH + user!!.uid)
        databaseReference.addListenerForSingleValueEvent (object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {}

            override fun onDataChange(snapshot: DataSnapshot) {
                try {
                    Log.e ("kek", snapshot.toString ())
                    progressMap = snapshot.value as HashMap<String, Any>
                } catch (e : TypeCastException) {
                    for (i in 1..4) progressMap[i.toString ()] = 0
                }
            }
        })
    }

    fun updProgress () {
        databaseReference = database.getReference (Constants.VIDEO_PATH + Constants.PROGRESS_PATH + user!!.uid)
        databaseReference.setValue (progressMap)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            1 -> {
                user = mAuth.currentUser
                if (user != null) {
                    supportFragmentManager.beginTransaction().replace(R.id.fragment_container, profileFragment).commit()
                    syncProgress ()
                }
            }
            CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE -> {
                profileFragment.photoCrop(resultCode, data)
            }
        }
    }

    fun backFromEpisode() {
        supportFragmentManager.beginTransaction ().replace (R.id.fragment_container, videoFragment).commit ()
    }

    fun backFromWatch () {
        supportFragmentManager.beginTransaction ().replace (R.id.fragment_container, episodeFragment).commit ()
    }

    override fun onSeasonClick(num: Int) {
//        Log.e ("kek", "season $num")
        supportFragmentManager.beginTransaction ().replace (R.id.fragment_container, episodeFragment).commit ()
        val sp = getSharedPreferences ("video", Context.MODE_PRIVATE)
        sp.edit ().putInt ("season", num + 1).apply ()
    }

    override fun onEpisodeClick(num: Int) {
//        Log.e("kek", "episode $num")
        supportFragmentManager.beginTransaction ().replace (R.id.fragment_container, watchFragment).commit ()
        val sp = getSharedPreferences ("video", Context.MODE_PRIVATE)
        sp.edit ().putInt ("episode", num + 1).apply ()
    }
}

/*
val ref : DatabaseReference = FirebaseDatabase.getInstance ().getReference ("${Constants.VIDEO_PATH}${Constants.PROGRESS_PATH}$currentUserID")
                    ref.setValue (progressMap).addOnCompleteListener { it1 ->
                        it1.addOnSuccessListener {
                            Toast.makeText(this, getString(R.string.registration_is_successful), Toast.LENGTH_SHORT).show()
                            finish()
                            Log.e ("kek", it.toString ())
                        }.addOnFailureListener {
                            Log.e("kek", it.toString())
                            Toast.makeText(this, getString(R.string.fail_message), Toast.LENGTH_SHORT).show()
                        }
                    }
 */