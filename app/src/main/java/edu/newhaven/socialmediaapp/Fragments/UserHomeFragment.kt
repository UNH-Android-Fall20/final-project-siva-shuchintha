package edu.newhaven.socialmediaapp.Fragments

import android.graphics.Color
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
import edu.newhaven.socialmediaapp.Adapter.PostItemAdapter
import edu.newhaven.socialmediaapp.Adapter.UserItemAdapter
import edu.newhaven.socialmediaapp.R
import edu.newhaven.socialmediaapp.models.Post
import edu.newhaven.socialmediaapp.models.User
import kotlinx.android.synthetic.main.fragment_current_post.view.*
import kotlinx.android.synthetic.main.postcard_homefrag.view.*

class UserHomeFragment : Fragment() {
    private var recyclerView: RecyclerView? = null
    private var postItemAdapter: PostItemAdapter? = null
    private lateinit var CurrentUser: FirebaseUser
    private var postList: MutableList<Post>? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_user_home, container, false)
        CurrentUser = FirebaseAuth.getInstance().currentUser!!
        recyclerView = view.findViewById(R.id.posts_homeFrag_recyclerView)
        recyclerView?.setHasFixedSize(true)
        recyclerView?.layoutManager = LinearLayoutManager(context)


        postList = ArrayList()
        postItemAdapter = context?.let { PostItemAdapter(it, postList as ArrayList<Post>, true) }
        recyclerView?.adapter = postItemAdapter
        getPostList()
        return view
    }

    private fun getPostList() {
        val usersRef = Firebase.firestore.collection("posts").orderBy("timestamp", Query.Direction.DESCENDING)
        usersRef.get().addOnSuccessListener { result ->
            postList?.clear()
            for (document in result) {
                Log.d("TAG22222", "${document.id} => ${document.data}")
                val post = document.toObject<Post>()
                if (post != null )
                {
                    postList?.add(post)
                }
            }
            postItemAdapter?.notifyDataSetChanged()
        }
            .addOnFailureListener { exception ->
                Log.d("TAG22222", "Error getting documents: ", exception)
            }
    }
}