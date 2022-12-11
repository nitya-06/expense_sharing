package com.example.expense_sharing

//import android.view.Menu
//import android.support.v4.view.Menu
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class HomeActivity : AppCompatActivity() {
    private lateinit var dbref: DatabaseReference
    private lateinit var triprecyclerview: RecyclerView
    private lateinit var triparraylist: ArrayList<trip>
    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient : GoogleSignInClient


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState);
        auth = FirebaseAuth.getInstance()
        setContentView(R.layout.activity_home)

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(this, gso)


        val email = intent.getStringExtra("email")
        val displayname = intent.getStringExtra("name")
//        findViewById<Button>(R.id.bsignout).setOnClickListener {
//            googleSignInClient.signOut()
//            startActivity(Intent(this, MainActivity::class.java))
//        }
//        findViewById<Button>(R.id.profile).setOnClickListener {
//            val intent = Intent(this@HomeActivity, ProfileActivity::class.java)
//            val acct = GoogleSignIn.getLastSignedInAccount(this)
//            val personName = acct!!.displayName
//            val personGivenName = acct!!.givenName
//            val email = acct!!.email
//            val personId = acct!!.id
//            val personPhoto = acct!!.photoUrl
//            intent.putExtra("name", personName)
//            intent.putExtra("email", email)
//            startActivity(intent)
//        }
        val imag = findViewById<ImageView>(R.id.add_btn_new)
        imag.setOnClickListener { addTrip() }
        triprecyclerview = findViewById(R.id.lvTripList)
        triprecyclerview.layoutManager = LinearLayoutManager(this)
        triprecyclerview.setHasFixedSize(true)

        triparraylist = arrayListOf<trip>()
        gettripdata()
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean{
        menuInflater.inflate(R.menu.menu_trip,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.profile-> showprofile()
            R.id.bsignout-> signout()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showprofile() {
        val intent = Intent(this@HomeActivity, ProfileActivity::class.java)
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

    private fun gettripdata(){
        dbref = FirebaseDatabase.getInstance().getReference("trip")
        dbref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    for(tripsnapshot in snapshot.children){
                        val trip = tripsnapshot.getValue(trip::class.java)
                        triparraylist.add(trip!!)
                    }
                    var adapter = tripadapter(triparraylist)
                    triprecyclerview.adapter = adapter
                    adapter.setonitemclicklistener(object : tripadapter.onitemClicklistener{
                        override fun onItemClick(position: Int) {
                            Toast.makeText(this@HomeActivity,"clicked on trip no $position",Toast.LENGTH_LONG).show()
                            var name = triparraylist.get(position).name.toString()
                            newpage(name)
                        }
                    })
                }
            }
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

    private fun newpage(name:String) {
        val intent = Intent(this,MemberActivity::class.java)
        intent.putExtra("tname",name)
        startActivity(intent)
    }

    private fun addTrip() {
        val intent = Intent(this,AddTrip::class.java)
        startActivity(intent)
    }
    override fun onBackPressed() {
            finish()
            return
    }
}

