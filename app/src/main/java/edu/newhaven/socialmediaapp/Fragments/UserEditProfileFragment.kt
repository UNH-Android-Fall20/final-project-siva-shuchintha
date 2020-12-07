package edu.newhaven.socialmediaapp.Fragments

import android.app.Activity
import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
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
import edu.newhaven.socialmediaapp.R
import kotlinx.android.synthetic.main.activity_edit_profile.*
import kotlinx.android.synthetic.main.fragment_user_profile.*

class UserEditProfileFragment : Fragment() {
    private var filepath: Uri? = null
    private var myUrl = ""
    var CurrentUser: FirebaseUser? = null
    private lateinit var auth: FirebaseAuth
    private lateinit var user: FirebaseUser
    var postUrl: String? = null
    var postDescription: String? = null
    var database: DocumentReference? = null
    private lateinit var storageReference: StorageReference
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view =  inflater.inflate(R.layout.fragment_user_edit_profile, container, false)
        auth = FirebaseAuth.getInstance()
        user = auth.currentUser!!
        database = Firebase.firestore.collection("users").document(auth.uid.toString())
        CurrentUser = FirebaseAuth.getInstance().currentUser!!

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
        back_to_profile_button.setOnClickListener {
        }
        return view
    }

    private fun DeleteAccount() {
        val builder = AlertDialog.Builder(activity!!)
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
                    Toast.makeText(activity!!, "Account Deleted Successfully", Toast.LENGTH_SHORT).show()
//                    startActivity(Intent(this@EditProfileActivity, MainActivity::class.java))
//                    finish()
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
                    Toast.makeText(activity!!, document.data.toString(), Toast.LENGTH_SHORT).show()
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
            var pd = ProgressDialog(activity!!)
            pd.setTitle("Saving...")
            pd.show()

            storageReference = FirebaseStorage.getInstance().getReference(
                CurrentUser!!.uid + ".jpg"
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
                        SaveChangesToDb(myUrl, pd)
                    }
                }
        }else{
            var pd = ProgressDialog(activity!!)
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
//            CHange it to fragment ==============================
//            val intent = Intent(activity!!, ::class.java)
//            startActivity(intent)
//            CHange it to fragment ==============================

            Toast.makeText(activity!!, "Successfully saved!", Toast.LENGTH_LONG).show()
        }.addOnFailureListener {
            pd.dismiss()
            Toast.makeText(activity!!, "Unsuccessfull!", Toast.LENGTH_LONG).show()
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
            Toast.makeText(activity!!, "Error image upload", Toast.LENGTH_LONG).show()
        }
    }


}