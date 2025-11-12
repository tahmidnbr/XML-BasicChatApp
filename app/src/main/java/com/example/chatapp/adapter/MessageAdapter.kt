package com.example.chatapp.adapter

import android.R
import android.text.Layout
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.chatapp.databinding.ItemMessageBinding
import com.example.chatapp.model.Message
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MessageAdapter(
    private val  messages: MutableList<Message>,
    private val currentUserId:  String
) : RecyclerView.Adapter<MessageAdapter.MessageViewHolder>() {
    class MessageViewHolder(val binding: ItemMessageBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MessageAdapter.MessageViewHolder {
        val binding = ItemMessageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MessageViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MessageAdapter.MessageViewHolder, position: Int) {
        val message = messages[position]
        val sdf = SimpleDateFormat("hh:mm a", Locale.getDefault())
        holder.binding.apply {
            msg.text = message.messageText
            user.text = message.senderName
            time.text = sdf.format(Date(message.timestamp))

            //ALLIGN LEFT RIGHT
            val params = holder.binding.cardmsg.layoutParams as FrameLayout.LayoutParams

            if (message.senderId == currentUserId) {
                params.gravity = Gravity.END
                //holder.binding.cardmsg.setBackgroundResource(R.drawable.bg_msg_sent)
                holder.binding.cardmsg.layoutDirection = View.LAYOUT_DIRECTION_RTL
            } else {
                params.gravity = Gravity.START
                holder.binding.cardmsg.layoutDirection = View.LAYOUT_DIRECTION_LTR
                //holder.binding.cardmsg.setBackgroundResource(R.drawable.bg_msg_received)
            }
            cardmsg.layoutParams = params

        }

    }

    override fun getItemCount(): Int = messages.size

    fun addMessage(msg: Message){
        messages.add(msg)
        notifyItemInserted(messages.size  -  1)
    }
}