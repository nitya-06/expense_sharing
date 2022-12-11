package com.example.expense_sharing

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth

class SplitActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient : GoogleSignInClient

    private var text = ""
    var tv: TextView? = null
    private var listMember= ArrayList<String>()
    fun minOfAmount(amount: Double, amount2: Double): Double {
        return if (amount < amount2) amount else amount2
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()
        setContentView(R.layout.activity_split)
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(this, gso)

        val intent  = intent
        val m1name = intent.getStringExtra("m1name")
        val m2name = intent.getStringExtra("m2name")
        val m3name = intent.getStringExtra("m3name")
        val m4name = intent.getStringExtra("m4name")
        val m5name = intent.getStringExtra("m5name")
        val m1paid = intent.getDoubleExtra("m1paid", 0.0)
        val m2paid = intent.getDoubleExtra("m2paid",0.0)
        val m3paid = intent.getDoubleExtra("m3paid",0.0)
        val m4paid = intent.getDoubleExtra("m4paid",0.0)
        val m5paid = intent.getDoubleExtra("m5paid",0.0)
        var count=0
        var t_sum =0.0
        var amount = DoubleArray(5)
        if(m1paid!=-1.0){
            amount[count]=m1paid
            if (m1name != null) {
                listMember.add(count,m1name)
            }
//            Log.i("info",amount[count].toString())

            count = count+1
            t_sum+=m1paid
        }
        if(m2paid!=-1.0){
            amount[count]=m2paid
            if (m2name != null) {
                listMember.add(count,m2name)
            }
//            Log.i("info",amount[count].toString())

            count = count+1
            t_sum+=m2paid
        }
        if(m3paid!=-1.0){
            amount[count]=m3paid
            if (m3name != null) {
                listMember.add(count,m3name)
            }
//            Log.i("info",amount[count].toString())

            count = count+1
            t_sum+=m3paid
        }
        if(m4paid!=-1.0){
            amount[count]=m4paid
            if (m4name != null) {
                listMember.add(count,m4name)
            }
//            Log.i("info",amount[count].toString())
            count = count+1
            t_sum+=m4paid
        }
        if(m5paid!=-1.0){
            amount[count]=m5paid
            if (m5name != null) {
                listMember.add(count,m5name)
            }
//            Log.i("info",amount[count].toString())

            count = count+1
            t_sum+=m5paid
        }
        Log.i("info",count.toString())
        t_sum = t_sum/count
        Log.i("info",t_sum.toString())

        for (i in 0 until count) {
            amount[i] = (amount[i]-t_sum)
            Log.i("info",amount[i].toString())
        }
        minCashFlowRec(amount,count)
        tv = findViewById<View>(R.id.tvSplit) as TextView
        tv!!.text = text
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
        val intent = Intent(this@SplitActivity, ProfileActivity::class.java)
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


    fun getMin(amount: DoubleArray,count:Int): Int {
        var minInd = 0
        for (i in 0 until count) if (amount[i] < amount[minInd]) minInd = i
        return minInd
    }
    fun getMax(arr: DoubleArray,count:Int): Int {
        var maxInd = 0
        for (i in 0 until count) if (arr[i] > arr[maxInd]) maxInd = i
        return maxInd
    }
    var ct=1
    fun minCashFlowRec(amount: DoubleArray,count:Int) {
        val mxCredit = getMax(amount,count)
        val mxDebit = getMin(amount,count)
        Log.i("info",amount[mxCredit].toString())
        Log.i("info",amount[mxDebit].toString())
        // If both amounts are 0, then all amounts are settled
        if (amount[mxCredit] == 0.0 || amount[mxDebit] == 0.0) return
        // Find the minimum of two amounts
        val min: Double = minOfAmount(-amount[mxDebit], amount[mxCredit])
        amount[mxCredit] -= min
        amount[mxDebit] += min
        text += """ $ct) ${
            listMember?.get(mxDebit)
        } have to pay Rs.$min to ${listMember?.get(mxCredit)}
"""
        ct++
        minCashFlowRec(amount,count)
    }


}
