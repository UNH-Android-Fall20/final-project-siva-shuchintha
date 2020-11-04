package edu.newhaven.socialmediaapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*

class Login : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        auth = FirebaseAuth.getInstance()

        button_Login.setOnClickListener {
            UserLogin()
        }
        textview_Signup.setOnClickListener {
            startActivity(Intent(this,SignUp::class.java))
            finish()
        }
    }

    private fun UserLogin() {
        if(Login_EmailAddress.text.toString().isEmpty()){
            Login_EmailAddress.error = "Email address missing!"
            Login_EmailAddress.requestFocus()
            return
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(Login_EmailAddress.text.toString()).matches()){
            Login_EmailAddress.error = "Enter valid Email!"
            Login_EmailAddress.requestFocus()
            return
        }
        if(Login_Password.text.toString().isEmpty()){
            Login_Password.error = "Password missing!"
            Login_Password.requestFocus()
            return
        }

        auth.signInWithEmailAndPassword(Login_EmailAddress.text.toString(), Login_Password.text.toString())
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this,"Login successfull!", Toast.LENGTH_LONG).show()
                    startActivity(Intent(this,TestingActivity::class.java))
                    finish()
                } else {
                    Toast.makeText(baseContext, "Login Failed!", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this,Login::class.java))
                    finish()
                }
            }
    }
}