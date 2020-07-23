package com.cherry.cherrypie

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class ChatActivity : AppCompatActivity() {

    private lateinit var model: Message
    private lateinit var sendButton: FloatingActionButton
    private lateinit var listOfMessages: RecyclerView
    private lateinit var adapter: MessageAdapter
    private lateinit var resultList: ArrayList<Message>
    private var s : Int = 1
    private var e : Int = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        s = intent.getIntExtra("season", 1)
        e = intent.getIntExtra("episode",1)

        resultList = ArrayList()

        sendButton = findViewById(R.id.btnSend)

        sendButton.setOnClickListener(View.OnClickListener {
            val textField = findViewById<EditText>(R.id.messageField)
            model = Message(
                FirebaseAuth.getInstance().currentUser!!.email,
                textField.text.toString()
            )
            if (textField.text.toString() == "") return@OnClickListener
            FirebaseDatabase.getInstance().reference.child("Messages").child(s.toString()).child(e.toString()).push()
                .setValue(
                    model
                )
            textField.setText("")
        })

        if (FirebaseAuth.getInstance().currentUser == null) {
            Toast.makeText(this@ChatActivity, "You should login", Toast.LENGTH_LONG).show()
            Log.d("CHECKER", "ChatActivity : user is not logged in")
            finish()
        } else {
            listOfMessages = findViewById(R.id.list_of_messages)
            listOfMessages.layoutManager = LinearLayoutManager(this)
            updateList()
            adapter = MessageAdapter(resultList)
            listOfMessages.adapter = adapter

        }
    }

    private fun getItemIndex(message: Message): Int {
        var index = -1
        for (i in 0 until resultList.size) {
            if (resultList[i].userName.equals(message.userName)) {
                index = i
                break
            }
        }
        return index
    }

    private fun updateList() {
        FirebaseDatabase.getInstance().reference.child("Messages").child(s.toString()).child(e.toString())
            .addChildEventListener(object : ChildEventListener {
                override fun onCancelled(error: DatabaseError) {
                    Log.d("CHECKER", "ChatActivity : ChildEventListener : onCancelled()")
                }

                override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                    Log.d("CHECKER", "ChatActivity : ChildEventListener : onChildMoved()")
                }

                override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                    Log.d("CHECKER", "ChatActivity : ChildEventListener : onChildChanged()")
                }

                override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                    resultList.add(snapshot.getValue(Message::class.java)!!)
                    adapter.notifyDataSetChanged()
                    Log.d("CHECKER", "ChatActivity : ChildEventListener : onChildAdded()")
                }

                override fun onChildRemoved(snapshot: DataSnapshot) {
                    var message = snapshot.getValue(Message::class.java)
                    var index = message?.let { getItemIndex(it) }
                    if (index != null) {
                        resultList.removeAt(index)
                    }
                    adapter.notifyDataSetChanged()
                    Log.d("CHECKER", "ChatActivity : ChildEventListener : onChildRemoved()")
                }

            })

    }
}




