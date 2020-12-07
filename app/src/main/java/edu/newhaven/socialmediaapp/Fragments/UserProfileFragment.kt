package edu.newhaven.socialmediaapp.Fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso
import edu.newhaven.socialmediaapp.EditProfileActivity
import edu.newhaven.socialmediaapp.R
import kotlinx.android.synthetic.main.activity_profile.*
import kotlinx.android.synthetic.main.activity_profile.view.*

class UserProfileFragment : Fragment() {
    private lateinit var CurrentUser: FirebaseUser

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_user_profile, container, false)
        CurrentUser = FirebaseAuth.getInstance().currentUser!!
        view.editProfileButton.setOnClickListener {
//            startActivity(Intent(this, EditProfileActivity::class.java))
//            finish()
        }
        FetchUserDetails()
    return view
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
                Log.d("tagabc","ddd "+ documents!!.isEmpty())

                for (document in documents) {
                    Log.d("tagabc", "${document.id} => ${document.data}")
                    count++
                }
                numberofFollowers_textView?.text = count.toString()

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
                Log.d("tagabc", "aaa"+result.documents.toString())

                for (document in result) {
                    Log.d("tagabc", "aaa"+"${document.id} => ${document.data}")

                    count++

                }
                numberofFollowing_textView.text = count.toString()

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
                        Log.d("TAG0", "DocumentSnapshot data: ${document.data!!["profileimage"].toString()}")
                        Picasso.get().load(document.data!!["profileimage"].toString()).into(ProfileImage_Imageview)
                    }
                    UserName_textView.setText(document.data!!["username"].toString())
                    FullName_textView.setText(document.data!!["fullname"].toString())
                    if(document.data!!["bio"].toString() !== ""){
                        Bio_textView.setText(document.data!!["bio"].toString())
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