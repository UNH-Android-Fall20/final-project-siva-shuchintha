package edu.newhaven.socialmediaapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.ActionBar
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class ProfileActivity: AppCompatActivity() {
    lateinit var toolbar: ActionBar
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //val db = Firebase.database


        toolbar = supportActionBar!!
        val bottomNavigation: BottomNavigationView = findViewById(R.id.bottomNavViewBar)
        bottomNavigation.setOnNavigationItemReselectedListener { item ->
            when(item.itemId) {
                R.id.ic_home -> {
                    // Respond to navigation item 1 reselection
                }
                R.id.ic_search -> {
                    // Respond to navigation item 2 reselection
                }
                R.id.ic_add -> {
                    //respond to navigation item 3 reselection
                }
                R.id.ic_activity ->{

                }
                R.id.ic_profile ->{
                    val i = Intent(this, ProfileActivity::class.java)
                    startActivity(i)
                }
            }
        }
    }
}