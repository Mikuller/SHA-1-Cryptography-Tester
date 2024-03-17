package com.mikucode.cryptography

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import java.security.MessageDigest

class TestActivity : AppCompatActivity() {
    private lateinit var edtVerifyMsg: EditText
    private lateinit var btnVerify: Button
    private lateinit var btnResetMsg: Button
    private lateinit var txtSavedHash: TextView
    private lateinit var sharedPreferences: SharedPreferences
    private fun initialization(){
        edtVerifyMsg = findViewById(R.id.edtVerifyMsg)
        btnVerify = findViewById(R.id.btnVerify)
        btnResetMsg = findViewById(R.id.btnReset)
        txtSavedHash = findViewById(R.id.txtSavedHash)
        sharedPreferences = getSharedPreferences("myPref", Context.MODE_PRIVATE)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)
        initialization()
        val savedHash = sharedPreferences.getString("hashCode", null)
        txtSavedHash.text = savedHash
        btnVerify.setOnClickListener {
            val testMessage = edtVerifyMsg.text.toString()
            val testMsgHashCode = sha1(testMessage)
            val savedHashCode = sharedPreferences.getString("hashCode", null)
            if(testMsgHashCode == savedHashCode){
                edtVerifyMsg.setText(testMsgHashCode)
                Snackbar.make(this@TestActivity, btnVerify,"Messages Matched!! Encryption Verified.",Snackbar.LENGTH_LONG)
                    .setActionTextColor(ContextCompat.getColor(this, R.color.teal_200))
                    .setAction("ReTest"){
                        val editor = sharedPreferences.edit()
                        editor.clear()
                        editor.apply()
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    }.show()
            }else{
                Snackbar.make(this@TestActivity, btnVerify,"Oops, Messages Doesn't Match!!",Snackbar.LENGTH_LONG)
                    .setActionTextColor(ContextCompat.getColor(this, R.color.teal_200))
                    .setAction("Try Again"){
                        edtVerifyMsg.text.clear()
                        recreate()
                    }.show()
            }
        }
        btnResetMsg.setOnClickListener {
            val editor = sharedPreferences.edit()
            editor.clear()
            editor.apply()
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

    }
    private fun sha1(input: String) = MessageDigest
        .getInstance("SHA-1")
        .digest(input.toByteArray())
        .fold("") { str, it -> str + "%02x".format(it) }  //this fold function returns 2 digit hexadecimal value for every byte of returned hashCode
    //str is accumulator and "it" is a single byte of the returned hashCode
    //This functions return a hexadecimal representation of the hashCode
}