package com.example.chatapp

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.chatapp.adapter.MessageAdapter
import com.example.chatapp.databinding.ActivityChatPageBinding
import com.example.chatapp.model.Message
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class ChatPageActivity : AppCompatActivity() {
    private lateinit var binding: ActivityChatPageBinding
    private lateinit var dbRef : DatabaseReference
    private lateinit var messageAdapter: MessageAdapter
    private val messages = mutableListOf<Message>()
    private var auth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding  = ActivityChatPageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val user =auth.currentUser
        if (user == null){
            startActivity(Intent(this, SignUpActivity::class.java))
            finish()
            return
        }

        messageAdapter = MessageAdapter(messages, user.uid)
        binding.recView.layoutManager = LinearLayoutManager(this).apply {
            stackFromEnd  = true
        }
        binding.recView.adapter = messageAdapter

        dbRef = FirebaseDatabase.getInstance().getReference("messages")
        dbRef.addChildEventListener(object : ChildEventListener{
            @SuppressLint("NotifyDataSetChanged")
            override fun onChildAdded(
                snapshot: DataSnapshot,
                previousChildName: String?
            ) {
                val msg = snapshot.getValue(Message::class.java)
                if (msg != null){
                    messages.add(msg)
                    messageAdapter.notifyDataSetChanged()
                    binding.recView.scrollToPosition(messageAdapter.itemCount - 1)
                }

            }

            override fun onChildChanged(
                snapshot: DataSnapshot,
                previousChildName: String?
            ) {
                TODO("Not yet implemented")
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                TODO("Not yet implemented")
            }

            override fun onChildMoved(
                snapshot: DataSnapshot,
                previousChildName: String?
            ) {
                TODO("Not yet implemented")
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@ChatPageActivity,  "DB  Error: ${error.message}", Toast.LENGTH_SHORT).show()
            }

        })

        binding.sentBtn.setOnClickListener { sendMessage() }

        binding.msgBox.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEND){
                sendMessage()
                true
            } else false
        }

    }

    private fun sendMessage() {
        val text = binding.msgBox.text.toString().trim()
        if (text.isEmpty()) return

        val user = auth.currentUser ?: return

        val displayName = user.email?.substringBefore("@")?: "User"

        val msg = Message(
            senderId = user.uid,
            senderName = displayName,
            messageText = text,
            timestamp = System.currentTimeMillis()
        )

        dbRef.push().setValue(msg).addOnCompleteListener { task ->
            if (task.isSuccessful){
                binding.msgBox.setText("")
                binding.recView.scrollToPosition(messageAdapter.itemCount - 1)
            }   else {
                Toast.makeText(this, "Send Failed: ${task.exception?.localizedMessage}", Toast.LENGTH_SHORT).show()

            }
        }
    }
}