package com.example.chatapp

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.chatapp.databinding.ActivitySignUpBinding
import com.google.firebase.auth.FirebaseAuth

class SignUpActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpBinding
    private var auth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.signUp.setOnClickListener {
            val email = binding.mail.text.toString()
            val pass = binding.pass.text.toString()

            auth.createUserWithEmailAndPassword(email,pass)
                .addOnCompleteListener {  task ->
                    if (task.isSuccessful){
                        Toast.makeText(this,  "Successful", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this , SignInActivity::class.java))
                    } else{
                        Toast.makeText(this,  "FAILED", Toast.LENGTH_SHORT).show()
                    }
                }

        }

        binding.already.setOnClickListener {
            startActivity(Intent(this, SignInActivity::class.java))
        }
    }
}