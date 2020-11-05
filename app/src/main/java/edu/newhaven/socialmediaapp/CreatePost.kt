package edu.newhaven.socialmediaapp

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.StorageTask
import com.google.firebase.storage.UploadTask
import edu.newhaven.socialmediaapp.models.Comment
import edu.newhaven.socialmediaapp.models.Post
import kotlinx.android.synthetic.main.activity_create_post.*

class CreatePost : AppCompatActivity() {

    lateinit var filepath: Uri
    private var myUrl = ""
    private var imageUri: Uri? = null
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_post)

        auth = FirebaseAuth.getInstance()

        selectImg.setOnClickListener {
            SelectImageFunc()
        }

        uploadImg.setOnClickListener {
            UploadPostWithDescription()
        }

    }

    private fun UploadPostWithDescription() {
        if (filepath != null) {
            var pd = ProgressDialog(this)
            pd.setTitle("Uploading...")
            pd.show()

            var imageFileRef: StorageReference = FirebaseStorage.getInstance().reference.child(
                System.currentTimeMillis().toString() + ".jpg"
            )
            var uploadTask: StorageTask<*>
            uploadTask = imageFileRef.putFile(filepath!!)

            var postURl =
                uploadTask.continueWithTask(Continuation<UploadTask.TaskSnapshot, Task<Uri>> { task ->
                    if (!task.isSuccessful) {
                        task.exception?.let {
                            throw it
                            pd.dismiss()
                        }
                    } else if (task.isSuccessful) {
                        val downloadUrl = task.result
                        myUrl = downloadUrl.toString()
                        PostToDatabase(myUrl,pd)
                    }
                    return@Continuation imageFileRef.downloadUrl
                })

        }
    }

    private fun PostToDatabase(myUrl: String, pd: ProgressDialog) {
        var database = Firebase.database.getReference("users").child(auth.uid.toString()).child("posts")
        val key = database.push().key
        if (key == null) {
            Log.w("TAG", "Couldn't get push key for posts")
        }
        var post = Post(auth.uid.toString(),"",description.text.toString(), 0, listOf(Comment("","")),myUrl)
        val postValues = post.toMap()
        val childUpdates = hashMapOf<String, Any>(
            "/$key" to postValues,
        )
        database.updateChildren(childUpdates).addOnCompleteListener {
            pd.dismiss()
            Toast.makeText(this, "Posted Successfully!", Toast.LENGTH_LONG).show()
            val i = Intent(this@CreatePost,CurrentPost::class.java)
            i.putExtra("URL",myUrl)
            startActivity(i)
        }.addOnFailureListener {
            pd.dismiss()
            Toast.makeText(this, "Post unsuccessfull!", Toast.LENGTH_LONG).show()
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