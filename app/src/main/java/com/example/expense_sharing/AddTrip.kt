package com.example.expense_sharing

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class AddTrip : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient : GoogleSignInClient

    lateinit var database : DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()
        setContentView(R.layout.activity_add_trip)
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(this, gso)

        val btn =findViewById<Button>(R.id.add_trip)
        btn.setOnClickListener{
            add_trip()
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
        val intent = Intent(this@AddTrip, ProfileActivity::class.java)
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

    private fun add_trip() {
        val tripn = findViewById<EditText>(R.id.trip_name)
        val tripname = tripn.text.toString().trim()
        if(tripname.isNotEmpty()){
            database = FirebaseDatabase.getInstance().getReference("trip")
            val trip_id = database.push().key
            val trip = trip(trip_id,tripname)
            if (trip_id != null) {
                database.child(trip_id).setValue(trip).addOnCompleteListener{
                    Toast.makeText(applicationContext,"trip added",Toast.LENGTH_LONG).show()
                    tripn.text.clear()
                    val intent = Intent(this,HomeActivity::class.java)
                    startActivity(intent)

                }
            }



        }
    }

}