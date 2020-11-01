package edu.newhaven.socialmediaapp

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    lateinit var filepath: Uri
    private var myUrl = ""
    private var imageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val db = Firebase.database
        val myref = db.getReference("message")
        myref.setValue("HI this is another test code for merge")

        selectImg.setOnClickListener {
            SelectImageFunc()
        }

    }

    private fun SelectImageFunc() {
        val i = Intent()
        i.setType("image/*")
        i.setAction(Intent.ACTION_GET_CONTENT)
        startActivityForResult(Intent.createChooser(i,"Select Image"), 1)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1 && resultCode == Activity.RESULT_OK && data != null) {
            filepath = data.data!!
            Log.d("Filepath",filepath.toString())
            imageView.setImageURI(filepath)
        } else {
            Toast.makeText(this, "Error image upload", Toast.LENGTH_LONG).show()
        }
    }
}
