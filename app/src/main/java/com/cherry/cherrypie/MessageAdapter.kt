package com.cherry.cherrypie

import android.text.format.DateFormat
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.message.view.*


class MessageAdapter(list : List<Message>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var messageList : List<Message> = list

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.message, parent, false)
        return MessageHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        var message : Message = messageList[position]

        (holder as MessageHolder).bind(message)
    }

    inner class MessageHolder internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(model : Message) {
            itemView.message_text.text = model.userName
                Log.e("CHECKER", "MessageAdapter: username " + model.userName)
            itemView.message_user.text = model.textMessage
                Log.e("CHECKER", "MessageAdapter: message " + model.textMessage)
            itemView.message_time.text = DateFormat.format(
                    "dd-MM-yyyy HH:mm:ss",
                    model.messageTime
                )
                Log.e("CHECKER", "MessageAdapter: time " + DateFormat.format(
                    "dd-MM-yyyy HH:mm:ss",
                    model.messageTime
                ))
        }
    }

    override fun getItemCount(): Int {
        return messageList.size
    }
}