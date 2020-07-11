package com.cherry.cherrypie

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class MainActivity : AppCompatActivity() {
    private lateinit var btn : Button
    private lateinit var profile : Button
    private lateinit var recycler : RecyclerView
    private lateinit var adapter : MainAdapter
    private lateinit var mAuth : FirebaseAuth
    private var user : FirebaseUser? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        btn = findViewById (R.id.addFood)
        profile = findViewById (R.id.profile)
        recycler = findViewById (R.id.mainRecycler)
        mAuth = FirebaseAuth.getInstance()
        user = mAuth.currentUser

        profile.setOnClickListener {
            if (user != null) {
                Toast.makeText (this, "${user!!.email}", Toast.LENGTH_SHORT).show ()
            } else {
                val intent = Intent (Constants.LOGIN_ACTIVITY_PATH)
                startActivityForResult (intent, 1)
            }
        }

        adapter = MainAdapter (this)
        recycler.adapter = adapter
        val llm = LinearLayoutManager (this)
        recycler.layoutManager = llm

        val item1 = FoodItem ("We")
        val item2 = FoodItem ("Are")
        val item3 = FoodItem ("Working")
        adapter.addItem (item1)
        adapter.addItem (item2)
        adapter.addItem (item3)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            1 -> {
                user = mAuth.currentUser
            }
        }
    }

}