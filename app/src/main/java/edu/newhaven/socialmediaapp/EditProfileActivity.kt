package edu.newhaven.socialmediaapp

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.widget.Toast
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.StorageTask
import edu.newhaven.socialmediaapp.models.Comment
import edu.newhaven.socialmediaapp.models.Post
import kotlinx.android.synthetic.main.activity_create_post.*
import kotlinx.android.synthetic.main.activity_edit_profile.*
import kotlinx.android.synthetic.main.activity_sign_up.*

class EditProfileActivity : AppCompatActivity() {
    lateinit var filepath: Uri
    var ref: DatabaseReference? = null
    private var myUrl = ""
    var UserFirebase: FirebaseUser? = null
    private lateinit var auth: FirebaseAuth
    var postUrl: String? = null
    var postDescription: String? = null
    var database: DocumentReference? = null
    private lateinit var storageReference: StorageReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)
        auth = FirebaseAuth.getInstance()
        database = Firebase.firestore.collection("users").document(auth.uid.toString())

        ShowInitialValues()

        profilePicChange_button.setOnClickListener {
            SelectImageFunc()
        }
        saveChanges_button.setOnClickListener {
            SaveProfileChanges()
        }
    }

    private fun ShowInitialValues(){
        database!!.get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    Log.d("TAG0", "DocumentSnapshot data: ${document.data}")
                    Log.d("TAG01", "DocumentSnapshot data: ${document.data!!["fullname"]}")

                    Toast.makeText(this,document.data.toString(),Toast.LENGTH_SHORT).show()
                    editFullName_text.setText(document.data!!["fullname"].toString())
                    if(document.data!!["bio"].toString() !== ""){
                        editBio_text.setText(document.data!!["bio"].toString())
                    }
                } else {
                    Log.d("TAG1", "No such document")
                }
            }
            .addOnFailureListener { exception ->
                Log.d("TAG2", "get failed with ", exception)
            }
    }

    private fun SaveProfileChanges() {
        if (filepath != null) {
            var pd = ProgressDialog(this)
            pd.setTitle("Saving...")
            pd.show()

            storageReference = FirebaseStorage.getInstance().getReference(
                 UserFirebase!!.uid + ".jpg")
            var uploadTask: StorageTask<*>
            uploadTask = storageReference.putFile(filepath!!)

            var postURl =
                uploadTask.continueWithTask { task ->1
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
                        SaveChangesToDb(myUrl,pd)
                    }
                }
        }
    }

    private fun SaveChangesToDb(myUrl: String, pd: ProgressDialog) {
        if(editFullName_text.text.toString().isEmpty()){
            SignUp_FullName.error = "Full Name missing!"
            SignUp_FullName.requestFocus()
            return
        }
        Toast.makeText(this,"prochange"  , Toast.LENGTH_LONG).show()
        val data = hashMapOf(
            "profileimage" to myUrl,
            "bio" to editBio_text.text.toString(),
            "fullname" to editFullName_text.text.toString()
        )

        database!!.set(data, SetOptions.merge()).addOnSuccessListener {
            pd.dismiss()
            val intent = Intent(this,ProfileActivity::class.java)
            startActivity(intent)
            Toast.makeText(this, "Successfully saved!", Toast.LENGTH_LONG).show()
        }.addOnFailureListener {
            pd.dismiss()
            Toast.makeText(this, "Unsuccessfull!", Toast.LENGTH_LONG).show()
        }
    }

    override fun onStart() {
        super.onStart()
        UserFirebase = FirebaseAuth.getInstance().currentUser
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
            profile_pic_edit.setImageURI(filepath)
        } else {
            Toast.makeText(this, "Error image upload", Toast.LENGTH_LONG).show()
        }
    }
}