package com.example.expense_sharing

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth

class MemberActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient : GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()

        setContentView(R.layout.activity_member)
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(this, gso)

        val intent = intent
        val trip_name = intent.getStringExtra("tname")
        findViewById<TextView>(R.id.TRIP).text = trip_name
        findViewById<Button>(R.id.submit).setOnClickListener{
            val m1 = findViewById<EditText>(R.id.m1name).text.toString().trim()
            val m2 = findViewById<EditText>(R.id.m2name).text.toString().trim()
            val m3 = findViewById<EditText>(R.id.m3name).text.toString().trim()
            val m4 = findViewById<EditText>(R.id.m4name).text.toString().trim()
            val m5 = findViewById<EditText>(R.id.m5name).text.toString().trim()
            val m1paid = findViewById<EditText>(R.id.m1paid).text.toString().trim().toDouble()
            val m2paid = findViewById<EditText>(R.id.m2paid).text.toString().trim().toDouble()
            val m3paid = findViewById<EditText>(R.id.m3paid).text.toString().trim().toDouble()
            val m4paid = findViewById<EditText>(R.id.m4paid).text.toString().trim().toDouble()
            val m5paid = findViewById<EditText>(R.id.m5paid).text.toString().trim().toDouble()
            var intent = Intent(this,SplitActivity::class.java)
//            Log.i("info",m1)
//            Log.i("info",m2)
//            Log.i("info",m3)
//            Log.i("info",m4)
//            Log.i("info",m5)
            intent.putExtra("m1name",m1)
            intent.putExtra("m2name",m2)
            intent.putExtra("m3name",m3)
            intent.putExtra("m4name",m4)
            intent.putExtra("m5name",m5)
            intent.putExtra("m1paid",m1paid)
            intent.putExtra("m2paid",m2paid)
            intent.putExtra("m3paid",m3paid)
            intent.putExtra("m4paid",m4paid)
            intent.putExtra("m5paid",m5paid)
            startActivity(intent)
        }
    }
//    override fun onCreateOptionsMenu(menu: Menu?): Boolean{
//        menuInflater.inflate(R.menu.menu_trip,menu)
//        return true
//    }
//
//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        when(item.itemId){
//            R.id.profile-> showprofile()
//            R.id.bsignout-> signout()
//        }
//        return super.onOptionsItemSelected(item)
//    }
    private fun showprofile() {
        val intent = Intent(this@MemberActivity, ProfileActivity::class.java)
        val acct = GoogleSignIn.getLastSignedInAccount(this)
        val personName = acct!!.displayName
        val personGivenName = acct!!.givenName
        val email = acct!!.email
        val personId = acct!!.id
        val personPhoto = acct!!.photoUrl
        intent.putExtra("name", personName)
        intent.putExtra("email", email)
        startActivity(intent)
    }

    private fun signout() {
        googleSignInClient.signOut()
        startActivity(Intent(this, MainActivity::class.java))
    }




}