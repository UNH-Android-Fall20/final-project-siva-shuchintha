package edu.newhaven.socialmediaapp.Fragments

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso
import edu.newhaven.socialmediaapp.Adapter.CommentsAdapter
import edu.newhaven.socialmediaapp.Adapter.PostItemAdapter
import edu.newhaven.socialmediaapp.R
import edu.newhaven.socialmediaapp.models.Comment
import edu.newhaven.socialmediaapp.models.Post
import kotlinx.android.synthetic.main.fragment_current_post.*
import kotlinx.android.synthetic.main.fragment_current_post.view.*
import java.text.SimpleDateFormat
import java.util.*

class CurrentPostFragment : Fragment() {
    private lateinit var CurrentUser: FirebaseUser
    private lateinit var postID: String
    private val data = hashMapOf("value" to true)
    private var recyclerView: RecyclerView? = null
    private var commentList: MutableList<Comment>? = null
    private var commentsAdapter: CommentsAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_current_post, container, false)

        CurrentUser = FirebaseAuth.getInstance().currentUser!!

        recyclerView = view.findViewById(R.id.post_comments_recyclerView)
        recyclerView?.setHasFixedSize(true)
        recyclerView?.layoutManager = LinearLayoutManager(context)
        commentList = ArrayList()
        commentsAdapter = context?.let { CommentsAdapter(it, commentList as ArrayList<Comment>, true) }
        recyclerView?.adapter = commentsAdapter
        val preference = context?.getSharedPreferences("POST", Context.MODE_PRIVATE)
        if (preference != null) {
            this.postID = preference.getString("PostID", "null")!!
            Log.d("tagabc","PostID"+ postID.toString())
            FetchPostDetails(postID)
        }

        view.CurrentLikes_post_button.setOnClickListener { AddUserLiked() }
        view.Currentsave_comments_button.setOnClickListener { saveComment(postID,CurrentaddComment_editText) }
        getCommentsList()
        return view
    }

    private fun getCommentsList() {
        Firebase.firestore.collection("posts")
            .document(postID)
            .collection("comments")
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener { result ->
            commentList?.clear()
            for (document in result) {
                Log.d("TAG22222", "${document.id} => ${document.data}")
                val comment = document.toObject<Comment>()
                if (comment != null )
                {
                    commentList?.add(comment)
                }
            }
            commentsAdapter?.notifyDataSetChanged()
        }
            .addOnFailureListener { exception ->
                Log.d("TAG22222", "Error getting documents: ", exception)
            }
    }


    private fun saveComment(postID: String, CurrentaddComment_editText: EditText) {
        if(CurrentaddComment_editText.text.toString().isEmpty()){
            CurrentaddComment_editText.error = "Comment missing!"
            CurrentaddComment_editText.requestFocus()
            return
        }

        var userName = ""
        Firebase.firestore.collection("users").document(CurrentUser!!.uid)
            .get().addOnSuccessListener { document ->
                if (document != null) {
                    Log.d("user name", document.data!!["username"].toString())
                    userName = document.data!!["username"].toString()
                    setCommentToFirestore(userName, CurrentaddComment_editText, postID)
                }else {
                    Log.d("post", "error finding doc")
                }
            }.addOnFailureListener { exception ->
                Log.d("post", "got failed with ", exception)
            }
    }

    private fun setCommentToFirestore(userName: String, CurrentaddComment_editText: EditText, postID: String) {
        Log.d("USERNAMEHERE", userName.toString())
        val dNow = Date()
        val ft = SimpleDateFormat("yyMMddhhmmssMs")
        var timestamp = ft.format(dNow)
        Log.d("USERNAMEHERE", userName.toString())

        var comment = Comment(userName.toString(), CurrentaddComment_editText.text.toString(),timestamp)
        val idcomment = timestamp + CurrentUser!!.uid
        Firebase.firestore.collection("posts")
            .document(postID)
            .collection("comments")
            .document(idcomment)
            .set(comment)
            .addOnCompleteListener {
                CurrentaddComment_editText.text.clear()

            }
        Log.d("comment", "Comment successfull")
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


