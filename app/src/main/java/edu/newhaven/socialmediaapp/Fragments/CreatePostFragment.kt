package edu.newhaven.socialmediaapp.Fragments

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.StorageTask
import edu.newhaven.socialmediaapp.BaseActivity
import edu.newhaven.socialmediaapp.R
import edu.newhaven.socialmediaapp.models.Post
import kotlinx.android.synthetic.main.activity_create_post.*
import kotlinx.android.synthetic.main.activity_create_post.view.*
import java.text.SimpleDateFormat
import java.util.*

class CreatePostFragment : Fragment() {

    lateinit var filepath: Uri
    private var myUrl = ""
    private var userName = ""
    private var imageUri: Uri? = null
    private lateinit var CurrentUser: FirebaseUser
    private lateinit var storageReference: StorageReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.activity_create_post, container, false)

        CurrentUser = FirebaseAuth.getInstance().currentUser!!
        getUserName()
        view?.selectImg?.setOnClickListener {
            SelectImageFunc()
        }

        view?.uploadImg?.setOnClickListener {
            UploadPostWithDescription()
        }
        return view

    }

    private fun getUserName() {
        Firebase.firestore.collection("users").document(CurrentUser.uid)
            .get().addOnSuccessListener { document ->
                if (document != null) {
                    userName = document.data!!["username"].toString()
                }else {
                    Log.d("post", "error finding doc")
                }

            }.addOnFailureListener { exception ->
                Log.d("post", "got failed with ", exception)
            }
    }

    private fun UploadPostWithDescription() {
        if (filepath != null) {
            var pd = ProgressDialog(context)
            pd.setTitle("Uploading...")
            pd.show()
            storageReference = FirebaseStorage.getInstance().getReference(
                System.currentTimeMillis().toString() + ".jpg"
            )
            var uploadTask: StorageTask<*>
            uploadTask = storageReference.putFile(filepath!!)

            var postURl =
                uploadTask.continueWithTask { task ->
                    if (!task.isSuccessful) {
                        task.exception?.let {
                            throw it
                            pd.dismiss()
                        }
                    }
                    storageReference!!.downloadUrl
                }.addOnCompleteListener{ task ->
                    if(task.isSuccessful){
                        val downloadUrl = task.result
                        myUrl = downloadUrl!!.toString()
                       // Toast.makeText(context, "url", Toast.LENGTH_SHORT).show()
                        //Toast.makeText(context, myUrl, Toast.LENGTH_SHORT).show()
                        PostToDatabase(myUrl, pd)
                    }
                }
        }
    }

    private fun PostToDatabase(myUrl: String, pd: ProgressDialog) {
        val dNow = Date()
        val ft = SimpleDateFormat("yyMMddhhmmssMs")
        var timestamp = ft.format(dNow)
        Log.d("post", timestamp.toString())
        var postId = timestamp.toString() + CurrentUser.uid

        var database = Firebase.firestore.collection("posts").document(postId)

        var post = Post(
            CurrentUser.uid,
            userName,
            title_edittext.text.toString(),
            myUrl,
            postId,
            timestamp.toString()
        )
        Toast.makeText(context, "post", Toast.LENGTH_LONG).show()

            database.set(post).addOnSuccessListener {
            pd.dismiss()
            val intent = Intent(context, BaseActivity::class.java)
            startActivity(intent)
            Toast.makeText(context, title_edittext.text.toString(), Toast.LENGTH_LONG).show()
            Toast.makeText(context, "Posted Successfully!", Toast.LENGTH_LONG).show()

        }.addOnFailureListener {
            pd.dismiss()

            Toast.makeText(context, "Post unsuccessfull!", Toast.LENGTH_LONG).show()
        }
    }

    private fun SelectImageFunc() {
        val i = Intent()
        i.setType("image/*")
        i.setAction(Intent.ACTION_GET_CONTENT)
        startActivityForResult(Intent.createChooser(i, "Select Image"), 1)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1 && resultCode == Activity.RESULT_OK && data != null) {
//            val uploadTask = storageReference!!.putFile()
            filepath = data.data!!
            Log.d("Filepath", filepath.toString())
            imageView.setImageURI(filepath)
        } else {
            Toast.makeText(context, "Error image upload", Toast.LENGTH_LONG).show()
        }
    }
}