package com.example.expense_sharing

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth

class ProfileActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient : GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()
        setContentView(R.layout.activity_profile)
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(this, gso)
        findViewById<Button>(R.id.S_out).setOnClickListener{
            signout()
        }
        val intent = intent
        val name = intent.getStringExtra("name")
        val email = intent.getStringExtra("email")
        val uri = intent.getStringExtra("photo_url")
        val textviewn = findViewById<TextView>(R.id.acct_name)
        val textviewe = findViewById<TextView>(R.id.acct_email)
        textviewn.text = name
        textviewe.text = email
        fun onBackPressed() {
            super.onBackPressed()
            val intent = Intent(this@ProfileActivity, HomeActivity::class.java)
            startActivity(intent)
        }

    }

    private fun signout() {
        googleSignInClient.signOut()
        startActivity(Intent(this, MainActivity::class.java))
    }
}