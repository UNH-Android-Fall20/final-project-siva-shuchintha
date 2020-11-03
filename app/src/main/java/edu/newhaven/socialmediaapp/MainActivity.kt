package edu.newhaven.socialmediaapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_sign_up.*

class MainActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        auth = FirebaseAuth.getInstance()

        textview_Signup.setOnClickListener {
            startActivity(Intent(this,SignUp::class.java))
            finish()
        }

        button_Login.setOnClickListener {
            UserLogin()
        }

//        Add this to navigation later
//        ToCreatePost.setOnClickListener {
//            val intent = Intent(this, CreatePost::class.java);
//            startActivity(intent);
//            finish()
//        }

    }

    private fun UserLogin() {
//        if(Login_EmailAddress.text.toString().isEmpty()){
//            Login_EmailAddress.error = "Email address missing!"
//            Login_EmailAddress.requestFocus()
//            return
//        }
//        if(!Patterns.EMAIL_ADDRESS.matcher(Login_EmailAddress.text.toString()).matches()){
//            Login_EmailAddress.error = "Enter valid Email!"
//            Login_EmailAddress.requestFocus()
//            return
//        }
//        if(Login_Password.text.toString().isEmpty()){
//            Login_Password.error = "Password missing!"
//            Login_Password.requestFocus()
//            return
//        }
//
//        auth.signInWithEmailAndPassword(Login_EmailAddress.text.toString(), Login_Password.text.toString())
//            .addOnCompleteListener(this) { task ->
//                if (task.isSuccessful) {
//                    Toast.makeText(this,"Login successfull",Toast.LENGTH_LONG).show()
//                    startActivity(Intent(this,TestingActivity::class.java))
//                    finish()
//                } else {
//                    Toast.makeText(baseContext, "Please Login", Toast.LENGTH_SHORT).show()
//
//                }
//            }
    }

    public override fun onStart() {
        super.onStart()

        val currentUser = auth.currentUser
        updateUI(currentUser)
    }

    private fun updateUI(currentUser: FirebaseUser?) {
        if(currentUser != null){
            startActivity(Intent(this, TestingActivity::class.java))
            finish()
           }
    }
}
