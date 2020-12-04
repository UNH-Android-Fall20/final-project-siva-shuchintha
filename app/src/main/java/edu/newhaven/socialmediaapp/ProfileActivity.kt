package edu.newhaven.socialmediaapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso
import edu.newhaven.socialmediaapp.models.User
import kotlinx.android.synthetic.main.activity_edit_profile.*
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_profile.*

class ProfileActivity: AppCompatActivity() {
    private lateinit var profileId: String
    private lateinit var CurrentUser: FirebaseUser


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        CurrentUser = FirebaseAuth.getInstance().currentUser!!

        editProfileButton.setOnClickListener {
            startActivity(Intent(this,EditProfileActivity::class.java))
            finish()
        }

        FetchUserDetails()
    }

    private fun FetchUserDetails() {
        FetchUsername_Bio_Fullname_Image()
    }

    private fun FetchUsername_Bio_Fullname_Image() {
        val usersRef = Firebase.firestore.collection("users").document(CurrentUser.uid)

        usersRef.get().addOnSuccessListener { document ->
                if (document != null) {
                    Log.d("TagUser", "DocumentSnapshot data: ${document.data}")
                    Log.d("TagUser", "DocumentSnapshot data: ${document.data!!["bio"]}")
                    if(document.data!!["profileimage"].toString() !== ""){
                        Log.d("TAG0", "DocumentSnapshot data: ${document.data!!["profileimage"].toString()}")
                        Picasso.get().load(document.data!!["profileimage"].toString()).into(ProfileImage_Imageview)
                    }
                    UserName_textView.setText(document.data!!["username"].toString())
                    FullName_textView.setText(document.data!!["fullname"].toString())
                    if(document.data!!["bio"].toString() !== ""){
                        Bio_textView.setText(document.data!!["bio"].toString())
                    }
                } else {
                    Log.d("TagUser", "error finding doc")
                }
            }
            .addOnFailureListener { exception ->
                Log.d("TagUser", "got failed with ", exception)
            }
    }
}