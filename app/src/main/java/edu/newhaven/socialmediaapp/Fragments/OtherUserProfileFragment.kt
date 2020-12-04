package edu.newhaven.socialmediaapp.Fragments

import android.content.Context
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
import kotlinx.android.synthetic.main.fragment_other_user_profile.*
import kotlinx.android.synthetic.main.fragment_other_user_profile.view.*


class OtherUserProfileFragment : Fragment() {
    private lateinit var OtherUser: String
    private lateinit var CurrentUser: FirebaseUser

//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        arguments?.let {
//        }
//    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
         val view = inflater.inflate(R.layout.fragment_other_user_profile, container, false)

        CurrentUser = FirebaseAuth.getInstance().currentUser!!

        val preference = context?.getSharedPreferences("USER", Context.MODE_PRIVATE)
        if (preference != null)
        {
            this.OtherUser = preference.getString("OtherUser", "null")!!
            Log.d("tagabc","pref"+ OtherUser.toString())
            FetchUserFollowStatus()
        }

        FetchUserDetails()
        return view
    }

    private fun FetchUserFollowStatus() {
        val followingRef =
            Firebase.firestore.collection("Follow").document(CurrentUser?.uid)
                .collection("Following")
        Log.d("tagabc", "profile11111 "+ OtherUser )
        followingRef.document(OtherUser).addSnapshotListener { snapshot, e ->
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
        FetchUsername_Bio_Fullname_Image()
    }

    private fun FetchUsername_Bio_Fullname_Image() {
        val usersRef = Firebase.firestore.collection("users").document(OtherUser)

        usersRef.get().addOnSuccessListener { document ->
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