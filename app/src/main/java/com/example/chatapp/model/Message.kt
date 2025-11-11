package com.example.chatapp.model

import com.google.firebase.Timestamp
import org.w3c.dom.Text

data class Message(
    var senderId: String? = null,
    var senderName: String? = null,
    var messageText: String? = null,
    var timestamp: Long = 0L
)
