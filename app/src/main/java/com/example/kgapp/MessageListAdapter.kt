package com.example.kgapp

import android.content.Context
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.net.toUri


class MessageListAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder> {
    override fun getItemCount(): Int {
        return mMessageList.size
    }

    companion object {
        val VIEW_TYPE_MESSAGE_SENT : Int = 1
        val VIEW_TYPE_MESSAGE_RECEIVED : Int = 2
    }

    private var context: Context
    private var mMessageList : ArrayList<UserMessage>
    private var currentUser: String


    constructor(ctx: Context, msgList: ArrayList<UserMessage>, currentUser: String){
        this.context = ctx
        this.mMessageList = msgList
        this.currentUser = currentUser
    }

    override fun getItemViewType(position: Int): Int {

        return if(mMessageList[position].sender.nickname.equals(this.currentUser)){
            VIEW_TYPE_MESSAGE_SENT
        } else {
            VIEW_TYPE_MESSAGE_RECEIVED
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view: View

        return if (viewType == VIEW_TYPE_MESSAGE_SENT) {
            view = LayoutInflater.from(parent.context).inflate(R.layout.item_message_sent, parent, false)
            SentMessageHolder(view)
        } else {
            view = LayoutInflater.from(parent.context).inflate(R.layout.item_message_received,parent,false)
            ReceivedMessageHolder(view)
        }

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val message = mMessageList[position]

        when (holder.itemViewType) {
            VIEW_TYPE_MESSAGE_SENT -> (holder as SentMessageHolder).bind(message)
            VIEW_TYPE_MESSAGE_RECEIVED -> (holder as ReceivedMessageHolder).bind(message)
        }

    }

    private class SentMessageHolder: RecyclerView.ViewHolder {
        var messageText: TextView
        var timeText : TextView

        constructor(itemView: View) : super(itemView) {

            messageText = itemView.findViewById(R.id.text_message_body)
            timeText = itemView.findViewById(R.id.text_message_time)
        }

        fun bind(message:UserMessage) {
            messageText.text = message.msg

            // Format the stored timestamp into a readable String using method.
            timeText.text = message.createdAt
        }
    }

    private class ReceivedMessageHolder: RecyclerView.ViewHolder{
        var messageText:TextView
        var timeText: TextView
        var nameText:TextView
        var profileImage:ImageView

        constructor(itemView: View) : super(itemView){
            messageText = itemView.findViewById (R.id.text_message_body)
            timeText = itemView.findViewById (R.id.text_message_time)
            nameText = itemView.findViewById (R.id.text_message_name)
            profileImage = itemView.findViewById (R.id.image_message_profile)
        }

        fun bind (message: UserMessage) {
            messageText.text = message.msg

            // Format the stored timestamp into a readable String using method.
            timeText.text =message.createdAt

            nameText.text = message.sender.nickname

            // Insert the profile image from the URL into the ImageView.
            profileImage.setImageURI(message.sender.profileUrl.toUri())
        }
    }
}