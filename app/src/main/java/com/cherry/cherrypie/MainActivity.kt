package com.cherry.cherrypie

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {
    private lateinit var btn : Button
    private lateinit var recycler : RecyclerView
    private lateinit var adapter : MainAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        btn = findViewById (R.id.addFood)
        recycler = findViewById (R.id.mainRecycler)
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
}