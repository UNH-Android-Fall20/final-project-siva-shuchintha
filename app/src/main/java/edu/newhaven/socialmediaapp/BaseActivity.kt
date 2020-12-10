package edu.newhaven.socialmediaapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import com.google.android.material.bottomnavigation.BottomNavigationView
import edu.newhaven.socialmediaapp.Fragments.CreatePostFragment
import edu.newhaven.socialmediaapp.Fragments.UserHomeFragment
import edu.newhaven.socialmediaapp.Fragments.UserProfileFragment
import edu.newhaven.socialmediaapp.Fragments.UserSearchFragment

class BaseActivity : AppCompatActivity() {
    lateinit var toolbar: ActionBar
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_base)

        getSupportActionBar()?.hide();
        val fragmentTrans = supportFragmentManager.beginTransaction()
        fragmentTrans.replace(R.id.fragment_container, UserHomeFragment())
        fragmentTrans.commit()

     //   toolbar = supportActionBar!!
        val bottomNavigation: BottomNavigationView = findViewById(R.id.bottomNavViewBar)
        bottomNavigation.setOnNavigationItemSelectedListener { item ->
            when(item.itemId) {
                R.id.ic_home -> {
                    val fragmentTrans = supportFragmentManager.beginTransaction()
                    fragmentTrans.replace(R.id.fragment_container, UserHomeFragment())
                    fragmentTrans.commit()
                    true

                }
                R.id.ic_search -> {
                    val fragmentTrans = supportFragmentManager.beginTransaction()
                    fragmentTrans.replace(R.id.fragment_container, UserSearchFragment())
                    fragmentTrans.commit()
                    true
                }
                R.id.ic_add -> {

                    val fragmentTrans = supportFragmentManager.beginTransaction()
                    fragmentTrans.replace(R.id.fragment_container,
                        CreatePostFragment()
                    )
                    fragmentTrans.commit()
                  //  startActivity(Intent(this, CreatePost::class.java))
                    true
                }
                R.id.ic_profile ->{
//                    startActivity(Intent(this, ProfileActivity::class.java))
////                    finish()
                    val fragmentTrans = supportFragmentManager.beginTransaction()
                    fragmentTrans.replace(R.id.fragment_container, UserProfileFragment())
                    fragmentTrans.commit()
                    true
                }
                else -> true
            }
        }
    }
    private var doubleBackToExitPressedOnce = false
    override fun onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed()
            return
        }

        this.doubleBackToExitPressedOnce = true
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show()

        Handler().postDelayed(Runnable { doubleBackToExitPressedOnce = false }, 2000)
    }
}