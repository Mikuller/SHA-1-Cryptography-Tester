package com.mikucode.cryptography

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import java.security.MessageDigest

class MainActivity : AppCompatActivity() {
    private lateinit var plainText: EditText
    private lateinit var btnDigest: Button
    private lateinit var sharedPreferences: SharedPreferences
    private fun  initialisation() {
          plainText =  findViewById(R.id.edtMessage)
          btnDigest =  findViewById(R.id.btnDigest)
          sharedPreferences = getSharedPreferences("myPref", Context.MODE_PRIVATE)
      }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initialisation()
        btnDigest.setOnClickListener {
            val plainText = plainText.text.toString()
            val hashCode = sha1(plainText)
            val editor = sharedPreferences.edit()
            editor.putString("hashCode", hashCode )
            editor.apply()
            val intent = Intent(this@MainActivity, TestActivity::class.java)
            startActivity(intent)
        }
    }
    private fun sha1(input: String) = MessageDigest
        .getInstance("SHA-1")
        .digest(input.toByteArray())
        .fold("") { str, it -> str + "%02x".format(it) }  //this fold function returns 2 digit hexadecimal value for every byte of returned hashCode
       //str is accumulator and "it" is a single byte of the returned hashCode
       //This functions return a hexadecimal representation of the hashCode

}