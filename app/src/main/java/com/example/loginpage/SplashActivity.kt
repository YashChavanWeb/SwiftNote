package com.example.loginpage

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class SplashActivity : AppCompatActivity() {

    // Declaring FirebaseAuth instance
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        // Initializing FirebaseAuth instance
        firebaseAuth = FirebaseAuth.getInstance()

        // Delay for the splash screen using Handler
        Handler().postDelayed({
            // Check if the user is already logged in
            if (firebaseAuth.currentUser != null) {
                // User is logged in, navigate to MainActivity
                startActivity(Intent(this, MainActivity::class.java))
            } else {
                // User is not logged in, navigate to LoginActivity
                startActivity(Intent(this, LoginActivity::class.java))
            }
            finish() // Finish the current SplashActivity
        }, 2000) // 2000 milliseconds (2 seconds) delay
    }
}
