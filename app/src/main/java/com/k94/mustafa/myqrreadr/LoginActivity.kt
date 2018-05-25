package com.k94.mustafa.myqrreadr

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Button

class LogActivity : AppCompatActivity() {

    lateinit var btn:Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        btn=findViewById(R.id.button2)
        btn.setOnClickListener {
            var intent=Intent(this,MainActivity::class.java)
            startActivity(intent)

        }
    }
}
