package com.example.kgapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var edttxt : EditText
    private lateinit var edttxt2 : EditText
    private lateinit var btnlg : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        edttxt = findViewById(R.id.edttxt)
        edttxt2 = findViewById(R.id.edttxt2)
        btnlg = findViewById(R.id.btnlg)

        btnlg.setOnClickListener {
            if(edttxt.text.length>5 && edttxt2.length()>5){
                val intent = Intent(this, MessageListActivity::class.java)
                println(edttxt.text)
                intent.putExtra("currentUser",edttxt.text.toString())
                intent.putExtra("targetUser", edttxt2.text.toString())
                startActivity(intent)
            }
        }

    }


}
