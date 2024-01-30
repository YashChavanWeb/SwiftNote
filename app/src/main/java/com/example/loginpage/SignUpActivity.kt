package com.example.loginpage

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.loginpage.databinding.ActivitySignUpBinding
import com.google.firebase.auth.FirebaseAuth

class SignUpActivity : AppCompatActivity() {

    // Initializing and assuring that it will not be null
    private lateinit var binding: ActivitySignUpBinding
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Converting XML to view objects
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize Firebase to use authentication services
        firebaseAuth = FirebaseAuth.getInstance()

        // Intent for login activity
        binding.bottom.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        // When the sign-up button is clicked
        binding.button.setOnClickListener {

            var email = binding.email.text.toString()
            var password = binding.password.text.toString()
            var rePassword = binding.repassword.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty() && rePassword.isNotEmpty()) {
                if (password == rePassword) {
                    // Creating a new user with email and password
                    firebaseAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener {
                            if (it.isSuccessful) {
                                val intent = Intent(this, MainActivity::class.java)
                                startActivity(intent)
                            } else {
                                // Display a toast message with the exception details if sign-up fails
                                Toast.makeText(this, "Account Cannot be created, please try again", Toast.LENGTH_SHORT).show()
                            }
                        }
                } else {
                    Toast.makeText(this, "Password Not Matching", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Input Fields Cannot be Empty", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
