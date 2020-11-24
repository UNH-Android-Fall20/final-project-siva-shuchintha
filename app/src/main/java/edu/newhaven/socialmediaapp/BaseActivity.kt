package edu.newhaven.socialmediaapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.ActionBar
import com.google.android.material.bottomnavigation.BottomNavigationView

class BaseActivity : AppCompatActivity() {
    lateinit var toolbar: ActionBar
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_base)

        toolbar = supportActionBar!!
        val bottomNavigation: BottomNavigationView = findViewById(R.id.bottomNavViewBar)
        bottomNavigation.setOnNavigationItemReselectedListener { item ->
            when(item.itemId) {
                R.id.ic_home -> {
                    val i = Intent(this, TestingActivity::class.java)
                    startActivity(i)
                }
                R.id.ic_search -> {
                    // Respond to navigation item 2 reselection
                }
                R.id.ic_add -> {
                    startActivity(Intent(this, CreatePost::class.java))
                    finish()
                }
                R.id.ic_activity ->{

                }
                R.id.ic_profile ->{
                    startActivity(Intent(this, ProfileActivity::class.java))
                    finish()
                }
            }
        }
    }
}