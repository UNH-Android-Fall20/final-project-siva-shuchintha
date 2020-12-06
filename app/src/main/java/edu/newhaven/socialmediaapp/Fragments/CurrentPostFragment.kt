package edu.newhaven.socialmediaapp.Fragments

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso
import edu.newhaven.socialmediaapp.R
import kotlinx.android.synthetic.main.fragment_current_post.*
import kotlinx.android.synthetic.main.fragment_current_post.view.*

class CurrentPostFragment : Fragment() {
    private lateinit var CurrentUser: FirebaseUser
    private lateinit var postID: String
    private val data = hashMapOf("value" to true)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_current_post, container, false)

        CurrentUser = FirebaseAuth.getInstance().currentUser!!
        val preference = context?.getSharedPreferences("POST", Context.MODE_PRIVATE)
        if (preference != null) {
            this.postID = preference.getString("PostID", "null")!!
            Log.d("tagabc","PostID"+ postID.toString())
            FetchPostDetails(postID)
        }

        view.CurrentLikes_post_button.setOnClickListener {
            AddUserLiked()
        }
        return view
    }

    private fun AddUserLiked() {
        if(CurrentLikes_post_button.text == "Like"){
            Firebase.firestore.collection("posts")
                .document(postID)
                .collection("likes")
                .document(CurrentUser!!.uid)
                .set(data)
        }else if(CurrentLikes_post_button.text == "Liked"){
            Firebase.firestore.collection("posts")
                .document(postID)
                .collection("likes")
                .document(CurrentUser!!.uid)
                .delete()
        }
    }

    private fun FetchPostDetails(postID: String) {
        Firebase.firestore.collection("posts").document(postID)
            .get().addOnSuccessListener { document ->
                Log.d("TAG22222", "${document.id} => ${document.data}")
                Toast.makeText(context, "${document.data}", Toast.LENGTH_SHORT).show()
                if (document != null) {
                Log.d("TAG22222", "${document.id} => ${document.data}")
                if(document.data!!["image"].toString() !== ""){
                    Picasso.get().load(document.data!!["image"].toString()).into(CurrentPostImage_imageView)
                }
                Currentotheruser_username_textView.setText(document.data!!["username"].toString())
                CurrentTitle_post_textView.setText(document.data!!["title"].toString())
                UpdateLikeStatus(postID)
            }
                Log.d("TAG22222", "${document.id} => ${document.data}")
            }.addOnFailureListener { exception ->
                Log.d("TAG22222", "Error getting documents: ", exception)
            }
        Log.d("TAG22222", "hhhhhhhhhtttttttytyttttttttytytytytytytytytytty")

    }

    private fun UpdateLikeStatus(postID: String) {
        Firebase.firestore.collection("posts")
            .document(postID)
            .collection("likes")
            .document(CurrentUser!!.uid)
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    Log.d("postadapter", "profil22efrag")
                    return@addSnapshotListener
                }
                Log.d("postadapter", "profil33efrag " + snapshot?.data.toString())

                if (snapshot != null && snapshot.exists()) {
                    CurrentLikes_post_button?.text = "Liked"
                    CurrentLikes_post_button.setBackgroundColor(Color.RED)
                    Log.d("currentpostadapter", "currentpostadapter " + snapshot?.exists().toString())
                } else {
                    CurrentLikes_post_button?.text = "Like"
                    CurrentLikes_post_button.setBackgroundColor(Color.BLACK)

                    Log.d("currentpostadapter", "currentpostadapter " + snapshot?.exists().toString())
                }
            }
    }
}


