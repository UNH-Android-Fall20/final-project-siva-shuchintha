package edu.newhaven.socialmediaapp.Fragments

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
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
import edu.newhaven.socialmediaapp.Adapter.UserPostsAdapter
import edu.newhaven.socialmediaapp.EditProfileActivity
import edu.newhaven.socialmediaapp.R
import edu.newhaven.socialmediaapp.TestingActivity
import edu.newhaven.socialmediaapp.models.Comment
import edu.newhaven.socialmediaapp.models.Post
import kotlinx.android.synthetic.main.fragment_user_profile.view.*
import java.util.ArrayList


class UserProfileFragment : Fragment() {
    private lateinit var CurrentUser: FirebaseUser
    private var recyclerView: RecyclerView? = null
    private var userPostList: MutableList<Post>? = null
    private var userPostAdapter: UserPostsAdapter? = null
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_user_profile, container, false)
        CurrentUser = FirebaseAuth.getInstance().currentUser!!
        view.editProfileButton.setOnClickListener {
            startActivity(Intent(activity, EditProfileActivity::class.java))
            (activity as Activity?)!!.overridePendingTransition(0, 0)
        }
        auth = FirebaseAuth.getInstance()

        recyclerView = view.findViewById(R.id.user_post_recyclerView)
        recyclerView?.setHasFixedSize(true)
        recyclerView?.layoutManager = LinearLayoutManager(context)
        userPostList = ArrayList()
        userPostAdapter = context?.let { UserPostsAdapter(it, userPostList as ArrayList<Post>, true) }
        recyclerView?.adapter = userPostAdapter
        FetchUserDetails()
        getUserPostList()
        view.Logout_button.setOnClickListener {
            auth.signOut()
        }
        return view
    }
    private fun getUserPostList() {
        Firebase.firestore.collection("posts").whereEqualTo("uid",CurrentUser!!.uid)
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener { result ->
                userPostList?.clear()
                for (document in result) {
                    Log.d("Userposts", "${document.id} => ${document.data}")
                    val post = document.toObject<Post>()
                    if (post != null )
                    {
                        userPostList?.add(post)
                    }
                }
                userPostAdapter?.notifyDataSetChanged()
            }
            .addOnFailureListener { exception ->
                Log.d("Userposts", "Error getting documents: ", exception)
            }
    }
    private fun FetchUserDetails() {
        GetUserDetailsFromUsersCollection()
        GetUserFollowCountFromFollowersCollection()
    }
    private fun GetUserFollowCountFromFollowersCollection() {
        FetchFollowCount()
        FetchFollowingCount()
    }
    private fun FetchFollowCount() {
        Log.d("tagabc", "followers profileId = " + CurrentUser.uid)
        var count = 0
        Firebase.firestore.collection("follow_and_following")
            .document(CurrentUser.uid)
            .collection("followersUid")
            .get().addOnSuccessListener { documents ->
                Log.d("tagabc", "ddd " + documents!!.isEmpty())

                for (document in documents) {
                    Log.d("tagabc", "${document.id} => ${document.data}")
                    count++
                }
                view?.numberofFollowers_textView?.text = count.toString()

            }
            .addOnFailureListener { exception ->
//                Log.w(TAG, "Error getting documents: ", exception)
            }
    }

    private fun FetchFollowingCount() {
        Log.d("tagabc", "followings profileId = " + CurrentUser.uid)
        var count = 0
        Firebase.firestore.collection("follow_and_following")
            .document(CurrentUser.uid)
            .collection("followingUid")
            .get().addOnSuccessListener { result ->
                Log.d("tagabc", "aaa" + result.documents.toString())

                for (document in result) {
                    Log.d("tagabc", "aaa" + "${document.id} => ${document.data}")

                    count++

                }
                view?.numberofFollowing_textView?.text = count.toString()

            }.addOnFailureListener { exception ->
                Log.d("tagabc", "Error getting documents: ", exception)
            }
    }

    private fun GetUserDetailsFromUsersCollection() {
        Firebase.firestore.collection("users")
            .document(CurrentUser.uid)
            .get().addOnSuccessListener { document ->
                if (document != null) {
                    Log.d("TagUser", "DocumentSnapshot data: ${document.data}")
                    Log.d("TagUser", "DocumentSnapshot data: ${document.data!!["bio"]}")
                    if(document.data!!["profileimage"].toString() !== ""){
                        Log.d(
                            "TAG0",
                            "DocumentSnapshot data: ${document.data!!["profileimage"].toString()}"
                        )
                        Picasso.get().load(document.data!!["profileimage"].toString()).into(view?.ProfileImage_Imageview)
                    }
                    view?.UserName_textView?.setText(document.data!!["username"].toString())
                    view?.FullName_textView?.setText(document.data!!["fullname"].toString())
                    if(document.data!!["bio"].toString() !== ""){
                        view?.Bio_textView?.setText(document.data!!["bio"].toString())
                    }
                } else {
                    Log.d("TagUser", "error finding doc")
                }
            }
            .addOnFailureListener { exception ->
                Log.d("TagUser", "got failed with ", exception)
            }
    }
}