package com.cherry.cherrypie

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*

class MainActivity : AppCompatActivity() {
    private lateinit var btn : Button
    private lateinit var profile : Button
    private lateinit var recycler : RecyclerView
    private lateinit var adapter : MainAdapter
    private lateinit var mAuth : FirebaseAuth
    private lateinit var database : FirebaseDatabase
    private lateinit var databaseReference : DatabaseReference
    private var user : FirebaseUser? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        btn = findViewById (R.id.addFood)
        profile = findViewById (R.id.profile)
        recycler = findViewById (R.id.mainRecycler)
        mAuth = FirebaseAuth.getInstance()
        user = mAuth.currentUser
        database = FirebaseDatabase.getInstance ()

        profile.setOnClickListener {
            if (user != null) {
                Toast.makeText (this, "${user!!.email}", Toast.LENGTH_SHORT).show ()
            } else {
                val intent = Intent (Constants.LOGIN_ACTIVITY_PATH)
                startActivityForResult (intent, 1)
            }
        }

        btn.setOnClickListener {
            val intent = Intent (Constants.ADD_FOOD_ACTIVITY_PATH)
            startActivityForResult (intent, 3)
        }

        adapter = MainAdapter (this)
        recycler.adapter = adapter
        val llm = LinearLayoutManager (this)
        recycler.layoutManager = llm

        feedMe ()

//        val item1 = FoodItem ("We")
//        val item2 = FoodItem ("Are")
//        val item3 = FoodItem ("Working")
//        adapter.addItem (item1)
//        adapter.addItem (item2)
//        adapter.addItem (item3)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            1 -> {
                user = mAuth.currentUser
            }
            3 -> {
                feedMe ()
            }
        }
    }

    private fun feedMe () {
        adapter.clearItems ()
        databaseReference = database.getReference ("${user!!.uid}/${Constants.FOOD_PATH}")
        databaseReference.addChildEventListener(object : ChildEventListener {
            override fun onCancelled(error: DatabaseError) {}

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {}

            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
//                Log.e ("kek", snapshot.toString ())
                val item = snapshot.getValue (FoodItem::class.java)
                adapter.addItem (item!!)
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {}
        })
    }

}