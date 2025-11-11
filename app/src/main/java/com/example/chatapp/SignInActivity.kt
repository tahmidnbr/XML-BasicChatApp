package com.example.chatapp

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.chatapp.databinding.ActivitySignInBinding
import com.google.firebase.auth.FirebaseAuth

class SignInActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignInBinding
    private val auth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivitySignInBinding.inflate(layoutInflater)

        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.signiN.setOnClickListener {
            val email = binding.mail.text.toString()
            val pass = binding.pass.text.toString()

            auth.signInWithEmailAndPassword(email,pass)
                .addOnCompleteListener {  task ->
                    if (task.isSuccessful){
                        Toast.makeText(this,  "Successful", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this ,ChatPageActivity::class.java))
                    } else{
                        Toast.makeText(this,  "FAILED", Toast.LENGTH_SHORT).show()
                    }
            }

        }
        binding.already.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
        }
    }
}