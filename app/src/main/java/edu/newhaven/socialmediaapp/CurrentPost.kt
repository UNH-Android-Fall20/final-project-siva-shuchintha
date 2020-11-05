package edu.newhaven.socialmediaapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class CurrentPost : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_current_post)

        val postUrl=intent.getStringExtra("URL")
    }

    override fun onStart() {
        super.onStart()

    }
}