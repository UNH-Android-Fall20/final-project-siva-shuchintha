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
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
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
    private lateinit var storageReference: StorageReference

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
                        Toast.makeText(this, "url" , Toast.LENGTH_SHORT).show()
                        Toast.makeText(this, myUrl , Toast.LENGTH_SHORT).show()
                        PostToDatabase(myUrl,pd)
                    }
                }
        }
    }

    private fun PostToDatabase(myUrl: String, pd: ProgressDialog) {

        var database = Firebase.firestore.collection("users").document(auth.uid.toString()).collection("posts")
//        val key = database.push().key
//        if (key == null) {
//            Log.w("TAG", "Couldn't get push key for posts")
//        }
        var post = Post(auth.uid.toString(),"",description.text.toString(), 0, listOf(Comment("","")),myUrl)
        val postValues = post.toMap()
//        val childUpdates = hashMapOf<String, Any>(
//            "/$filepath" to postValues,
//        )
        Toast.makeText(this,"post"  , Toast.LENGTH_LONG).show()

        database.document(Timestamp.now().toString()).set(post).addOnSuccessListener {
            pd.dismiss()
            val intent = Intent(this@CreatePost,CurrentPost::class.java)
            intent.putExtra("URL",myUrl)
            intent.putExtra("Description",description.text.toString())
            startActivity(intent)
            Toast.makeText(this,description.text.toString(),Toast.LENGTH_LONG ).show()
            Toast.makeText(this, "Posted Successfully!", Toast.LENGTH_LONG).show()

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
//            val uploadTask = storageReference!!.putFile()
            filepath = data.data!!
            Log.d("Filepath",filepath.toString())
            imageView.setImageURI(filepath)
        } else {
            Toast.makeText(this, "Error image upload", Toast.LENGTH_LONG).show()
        }
    }
}