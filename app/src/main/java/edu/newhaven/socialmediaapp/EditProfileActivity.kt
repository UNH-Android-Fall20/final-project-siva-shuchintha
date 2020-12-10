package edu.newhaven.socialmediaapp

import android.app.Activity
import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
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
import com.squareup.picasso.Picasso
import edu.newhaven.socialmediaapp.Fragments.UserHomeFragment
import edu.newhaven.socialmediaapp.Fragments.UserProfileFragment
import kotlinx.android.synthetic.main.activity_edit_profile.*
import kotlinx.android.synthetic.main.fragment_user_profile.*
import edu.newhaven.socialmediaapp.R


class EditProfileActivity : AppCompatActivity() {
    private var filepath: Uri? = null
    var ref: DatabaseReference? = null
    private var myUrl = ""
    var UserFirebase: FirebaseUser? = null
    private lateinit var auth: FirebaseAuth
    private lateinit var user: FirebaseUser
    var postUrl: String? = null
    var postDescription: String? = null
    var database: DocumentReference? = null
    private lateinit var storageReference: StorageReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)
        auth = FirebaseAuth.getInstance()
        user = auth.currentUser!!
        database = Firebase.firestore.collection("users").document(auth.uid.toString())

        ShowInitialValues()
        filepath = null
        profilePicChange_button.setOnClickListener {
            SelectImageFunc()
        }
        saveChanges_button.setOnClickListener {
            SaveProfileChanges()
        }

        deleteAcc_button.setOnClickListener {
            DeleteAccount()
        }
    }

    override fun onStart() {
        super.onStart()
        UserFirebase = FirebaseAuth.getInstance().currentUser
    }

    private fun DeleteAccount() {
        val builder = AlertDialog.Builder(this@EditProfileActivity)
        builder.setMessage("Are you sure you want to Delete the account?")
            .setCancelable(false)
            .setPositiveButton("Yes") { dialog, id ->
                DeleteUserAuth()

            }
            .setNegativeButton("No") { dialog, id ->
                dialog.dismiss()
            }
        val alert = builder.create()
        alert.show()
    }

    private fun DeleteUserAuth(){
        database!!.delete()
            .addOnSuccessListener {
                Log.d("DeleteAccSuccess", "Account successfully deleted!")
                DeleteUserDataFirestore()
            }
            .addOnFailureListener { e -> Log.w("DeleteAccError", "Error deleting Account", e) }
    }

    private fun DeleteUserDataFirestore(){
        user.delete()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d("TAG", "User Auth deleted!")
                    Toast.makeText(this, "Account Deleted Successfully", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this@EditProfileActivity, MainActivity::class.java))
                    finish()
                }
            }.addOnFailureListener { e ->
                Log.e("TAG", e.message, e)
            }
    }

    private fun ShowInitialValues(){
        database!!.get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    Log.d("TAG0", "DocumentSnapshot data: ${document.data}")
                    Log.d("TAG01", "DocumentSnapshot data: ${document.data!!["bio"]}")
                    if(document.data!!["profileimage"].toString() !== ""){
                        myUrl = document.data!!["profileimage"].toString()
                        Picasso.get().load(myUrl).into(profile_pic_edit)

                    }
                   // Toast.makeText(this, document.data.toString(), Toast.LENGTH_SHORT).show()
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
                UserFirebase!!.uid + ".jpg"
            )
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
                        Toast.makeText(this, "url", Toast.LENGTH_SHORT).show()
                       // Toast.makeText(this, myUrl, Toast.LENGTH_SHORT).show()
                        SaveChangesToDb(myUrl, pd)
                    }
                }
        }else{
            var pd = ProgressDialog(this)
            pd.setTitle("Saving...")
            pd.show()
            SaveChangesToDb(myUrl, pd)

        }
    }

    private fun SaveChangesToDb(myUrl: String, pd: ProgressDialog) {
        if(editFullName_text.text.toString().isEmpty()){
            editFullName_text.error = "Full Name missing!"
            editFullName_text.requestFocus()
            return
        }
        if(editBio_text.text.toString().isEmpty()){
            editBio_text.error = "Bio missing!"
            editBio_text.requestFocus()
            return
        }
        val data = hashMapOf(
            "profileimage" to myUrl,
            "bio" to editBio_text.text.toString(),
            "fullname" to editFullName_text.text.toString()
        )

        database!!.set(data, SetOptions.merge()).addOnSuccessListener {
            pd.dismiss()
              finish()
            Toast.makeText(this, "Successfully saved!", Toast.LENGTH_LONG).show()
        }.addOnFailureListener {
            pd.dismiss()
            Toast.makeText(this, "Unsuccessfull!", Toast.LENGTH_LONG).show()
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
            filepath = data.data!!
            Log.d("Filepath", filepath.toString())
            profile_pic_edit.setImageURI(filepath)
        } else {
            Toast.makeText(this, "Error image upload", Toast.LENGTH_LONG).show()
        }
    }

}
