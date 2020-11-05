package edu.newhaven.socialmediaapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import edu.newhaven.socialmediaapp.models.Post
import edu.newhaven.socialmediaapp.models.User
import kotlinx.android.synthetic.main.activity_sign_up.*

class SignUp : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference

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
        if(SignUp_FullName.text.toString().isEmpty()){
            SignUp_FullName.error = "Full Name missing!"
            SignUp_FullName.requestFocus()
            return
        }
        if(SignUp_UserName.text.toString().isEmpty()){
            SignUp_UserName.error = "User Name missing!"
            SignUp_UserName.requestFocus()
            return
        }
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
                    onAuthSuccess(task.result?.user!!)

                } else {
                    Toast.makeText(baseContext, "Sign up failed, try later!",
                        Toast.LENGTH_SHORT).show()
                }
            }

    }

    private fun onAuthSuccess(user: FirebaseUser) {

        val userdetail = User(user.uid.toString(),SignUp_FullName.text.toString(),SignUp_UserName.text.toString(),user.email,
            null,null,null,"")

        Toast.makeText(this, "Sign up successfull!",Toast.LENGTH_LONG).show()
        writeNewUser(userdetail)
        auth.signOut()
        startActivity(Intent(this,Login::class.java))
        finish()
    }

    private fun writeNewUser(userdetail:User) {
        database = Firebase.database.getReference("users")
        Log.d("user3",userdetail.toString())
        database.child(userdetail.uid).setValue(userdetail)
    }
}