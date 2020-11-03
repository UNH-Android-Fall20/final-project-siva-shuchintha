package edu.newhaven.socialmediaapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_sign_up.*

class SignUp : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        auth = FirebaseAuth.getInstance()

        button_Signup.setOnClickListener {
            userSignUp()
        }
        textview_Login.setOnClickListener {
            startActivity(Intent(this,MainActivity::class.java))
            finish()
        }

    }

    private fun userSignUp() {
        if(SignUp_EmailAddress.text.toString().isEmpty()){
            SignUp_EmailAddress.error = "Email address missing!"
            SignUp_EmailAddress.requestFocus()
            return
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(SignUp_EmailAddress.text.toString()).matches()){
            SignUp_EmailAddress.error = "Enter valid Email!"
            SignUp_EmailAddress.requestFocus()
            return
        }
        if(SignUp_Password.text.toString().isEmpty()){
            SignUp_Password.error = "Password missing!"
            SignUp_Password.requestFocus()
            return
        }

        auth.createUserWithEmailAndPassword(SignUp_EmailAddress.text.toString(), SignUp_Password.text.toString())
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Toast.makeText(baseContext, "Sign up successfull!",Toast.LENGTH_LONG).show()
                    auth.signOut()
                    startActivity(Intent(this,MainActivity::class.java))
                    finish()
                } else {
                    Toast.makeText(baseContext, "Sign up failed, try later!",
                        Toast.LENGTH_SHORT).show()
                }
            }

    }
}