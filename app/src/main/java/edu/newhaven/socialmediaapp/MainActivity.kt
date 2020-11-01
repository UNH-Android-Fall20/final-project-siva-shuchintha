package edu.newhaven.socialmediaapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val db = Firebase.database
        val myref = db.getReference("message")
        myref.setValue("HI this is another test code for merge")

//        Add this to navigation later
        ToCreatePost.setOnClickListener {
            val intent = Intent(this, CreatePost::class.java);
            startActivity(intent);
        }
    }
}