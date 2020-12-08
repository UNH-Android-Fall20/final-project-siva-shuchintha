package edu.newhaven.socialmediaapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.ActionBar
import com.google.android.material.bottomnavigation.BottomNavigationView
import edu.newhaven.socialmediaapp.Fragments.UserHomeFragment
import edu.newhaven.socialmediaapp.Fragments.UserProfileFragment
import edu.newhaven.socialmediaapp.Fragments.UserSearchFragment
import kotlinx.android.synthetic.main.postcard_homefrag.*

class BaseActivity : AppCompatActivity() {
    lateinit var toolbar: ActionBar
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_base)
        val fragmentTrans = supportFragmentManager.beginTransaction()
        fragmentTrans.replace(R.id.fragment_container, UserHomeFragment())
        fragmentTrans.commit()
        toolbar = supportActionBar!!
        val bottomNavigation: BottomNavigationView = findViewById(R.id.bottomNavViewBar)
        bottomNavigation.setOnNavigationItemReselectedListener { item ->
            when(item.itemId) {
                R.id.ic_home -> {
                    val fragmentTrans = supportFragmentManager.beginTransaction()
                    fragmentTrans.replace(R.id.fragment_container, UserHomeFragment())
                    fragmentTrans.commit()

                }
                R.id.ic_search -> {
                    val fragmentTrans = supportFragmentManager.beginTransaction()
                    fragmentTrans.replace(R.id.fragment_container, UserSearchFragment())
                    fragmentTrans.commit()
                }
                R.id.ic_add -> {
                    startActivity(Intent(this, CreatePost::class.java))
                    finish()
                }
                R.id.ic_activity ->{
                    val i = Intent(this, TestingActivity::class.java)
                    startActivity(i)
                }
                R.id.ic_profile ->{
//                    startActivity(Intent(this, ProfileActivity::class.java))
////                    finish()
                    val fragmentTrans = supportFragmentManager.beginTransaction()
                    fragmentTrans.replace(R.id.fragment_container, UserProfileFragment())
                    fragmentTrans.commit()
                }
            }
        }
    }
}