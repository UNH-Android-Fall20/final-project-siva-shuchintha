package edu.newhaven.socialmediaapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.squareup.picasso.Picasso
import edu.newhaven.socialmediaapp.models.User
import kotlinx.android.synthetic.main.activity_current_post.*

class CurrentPost : AppCompatActivity() {
    var ref: DatabaseReference? = null
    var UserFirebase: FirebaseUser? = null
    var postUrl: String? = null
    var postDescription: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_current_post)
        val intent = intent
        postUrl=intent.getStringExtra("URL")
        postDescription=intent.getStringExtra("Description")
    }

    override fun onStart() {
        super.onStart()

        UserFirebase = FirebaseAuth.getInstance().currentUser
        ref = FirebaseDatabase.getInstance().reference.child("users").child(UserFirebase!!.uid).child("username")
        val user :String = ref!!.push().key.toString()
        textView_UserName.setText(UserFirebase?.email)
        Picasso.get()
            .load(postUrl)
            .resize(50, 50)
            .centerCrop()
            .placeholder(R.drawable.ic_logo)
            .into(imageView_post)
        textView_Description.setText(postDescription.toString())
    }
}