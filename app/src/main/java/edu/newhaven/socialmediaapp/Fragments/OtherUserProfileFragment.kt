package edu.newhaven.socialmediaapp.Fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso
import edu.newhaven.socialmediaapp.Adapter.UserPostsAdapter
import edu.newhaven.socialmediaapp.R
import edu.newhaven.socialmediaapp.models.Post
import kotlinx.android.synthetic.main.fragment_other_user_profile.*
import kotlinx.android.synthetic.main.fragment_other_user_profile.view.*
import kotlinx.android.synthetic.main.fragment_user_profile.view.*
import java.util.ArrayList


class OtherUserProfileFragment : Fragment() {
    private lateinit var OtherUser: String
    private lateinit var CurrentUser: FirebaseUser
    private val data = hashMapOf("value" to "true")
    private var recyclerView: RecyclerView? = null
    private var otherUserPostList: MutableList<Post>? = null
    private var userPostAdapter: UserPostsAdapter? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
         val view = inflater.inflate(R.layout.fragment_other_user_profile, container, false)

        CurrentUser = FirebaseAuth.getInstance().currentUser!!
        val preference = context?.getSharedPreferences("USER", Context.MODE_PRIVATE)
        if (preference != null) {
            this.OtherUser = preference.getString("OtherUser", "null")!!
            Log.d("tagabc","pref"+ OtherUser.toString())
            FetchUserFollowStatus()
        }
        view.followButtonOtherUser.setOnClickListener {
            if (view.followButtonOtherUser.text == "Follow"){
                AddUserToFollowList()
            }else if (view.followButtonOtherUser.text == "Following") {
                RemoveUserFromFollowList()
            }
        }
        recyclerView = view.findViewById(R.id.otheruser_post_recyclerView)
        recyclerView?.setHasFixedSize(true)
        recyclerView?.layoutManager = LinearLayoutManager(context)
        otherUserPostList = ArrayList()
        userPostAdapter = context?.let { UserPostsAdapter(it, otherUserPostList as ArrayList<Post>, true) }
        recyclerView?.adapter = userPostAdapter
        FetchUserDetails()
        getOtherUserPostList()

        return view
    }

    private fun getOtherUserPostList() {
        Firebase.firestore.collection("posts").whereEqualTo("uid",OtherUser!!)
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener { result ->
                otherUserPostList?.clear()
                var count = 0

                for (document in result) {
                    Log.d("otherUserposts", "${document.id} => ${document.data}")
                    val post = document.toObject<Post>()
                    count++

                    if (post != null )
                    {
                        otherUserPostList?.add(post)
                    }
                }
                view?.numberofPostsOtherUser_textView?.text = count.toString()
                userPostAdapter?.notifyDataSetChanged()
            }
            .addOnFailureListener { exception ->
                Log.d("otherUserposts", "Error getting documents: ", exception)
            }
    }

    private fun AddUserToFollowList() {
            Firebase.firestore.collection("follow_and_following").document(CurrentUser.uid.toString())
                .collection("followingUid").document(OtherUser)
                .set(data)

            Firebase.firestore.collection("follow_and_following").document(OtherUser)
                .collection("followersUid").document(CurrentUser.uid.toString())
                .set(data).addOnSuccessListener { view?.followButtonOtherUser?.text = "Following" }
    }

    private fun RemoveUserFromFollowList() {
        Firebase.firestore.collection("follow_and_following").document(CurrentUser.uid.toString())
            .collection("followingUid").document(OtherUser)
            .delete()

        Firebase.firestore.collection("follow_and_following").document(OtherUser)
            .collection("followersUid").document(CurrentUser.uid.toString())
            .delete().addOnSuccessListener { view?.followButtonOtherUser?.text = "Follow" }
    }

    private fun FetchUserFollowStatus() {
        Log.d("tagabc", "profile11111 "+ OtherUser )
        Firebase.firestore.collection("follow_and_following").document(CurrentUser?.uid)
                .collection("followingUid").document(OtherUser).addSnapshotListener { snapshot, e ->
            if (e != null) {
                Log.d("tagabc", "profil22efrag")
                return@addSnapshotListener
            }
            Log.d("tagabc", "profil33efrag " + snapshot?.data.toString())

            if (snapshot != null && snapshot.exists()) {
                view?.followButtonOtherUser?.text = "Following"
                Log.d("tagabc", "profil33e111frag " + snapshot?.exists().toString())

            } else {
                view?.followButtonOtherUser?.text = "Follow"
                Log.d("tagabc", "profil33efrag " + snapshot?.exists().toString())

            }
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
        Log.d("tagabc", "followers profileId = " + OtherUser)
        var count = 0
        Firebase.firestore.collection("follow_and_following")
            .document(OtherUser)
            .collection("followersUid")
            .get().addOnSuccessListener { documents ->
            Log.d("tagabc","ddd "+ documents!!.isEmpty())

            for (document in documents) {
                Log.d("tagabc", "${document.id} => ${document.data}")
                count++
            }
            view?.numberofFollowersOtherUser_textView?.text = count.toString()

        }
            .addOnFailureListener { exception ->
//                Log.w(TAG, "Error getting documents: ", exception)
            }
    }

    private fun FetchFollowingCount() {
        Log.d("tagabc", "followings profileId = " + OtherUser)
        var count = 0
        Firebase.firestore.collection("follow_and_following")
            .document(OtherUser)
            .collection("followingUid")
            .get().addOnSuccessListener { result ->
            Log.d("tagabc", "aaa"+result.documents.toString())

            for (document in result) {
                Log.d("tagabc", "aaa"+"${document.id} => ${document.data}")

                count++

            }
            view?.numberofFollowingOtherUser_textView?.text = count.toString()

        }.addOnFailureListener { exception ->
            Log.d("tagabc", "Error getting documents: ", exception)
        }
    }

    private fun GetUserDetailsFromUsersCollection() {
        Firebase.firestore.collection("users")
            .document(OtherUser)
            .get().addOnSuccessListener { document ->
            if (document != null) {
                Log.d("TagUser", "DocumentSnapshot data: ${document.data}")
                Log.d("TagUser", "DocumentSnapshot data: ${document.data!!["bio"]}")
                if(document.data!!["profileimage"].toString() !== ""){
                    Log.d("TAG0", "DocumentSnapshot data: ${document.data!!["profileimage"].toString()}")
                    Picasso.get().load(document.data!!["profileimage"].toString()).into(ProfileImageOtherUser_Imageview)
                }
                UserNameOtherUser_textView.setText(document.data!!["username"].toString())
                FullNameOtherUser_textView.setText(document.data!!["fullname"].toString())
                if(document.data!!["bio"].toString() !== ""){
                    BioOtherUser_textView.setText(document.data!!["bio"].toString())
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