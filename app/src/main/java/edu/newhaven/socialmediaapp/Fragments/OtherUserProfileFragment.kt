package edu.newhaven.socialmediaapp.Fragments

import android.content.Context
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
import edu.newhaven.socialmediaapp.R
import kotlinx.android.synthetic.main.fragment_other_user_profile.*
import kotlinx.android.synthetic.main.fragment_other_user_profile.view.*


class OtherUserProfileFragment : Fragment() {
    private lateinit var OtherUser: String
    private lateinit var CurrentUser: FirebaseUser
    private val data = hashMapOf("value" to "true")
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
        FetchUserDetails()
        return view
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
        val followingRef =
            Firebase.firestore.collection("follow_and_following").document(CurrentUser?.uid)
                .collection("followingUid")
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