package edu.newhaven.socialmediaapp.Fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import edu.newhaven.socialmediaapp.Adapter.UserItemAdapter
import edu.newhaven.socialmediaapp.R
import edu.newhaven.socialmediaapp.models.User

class UserSearchFragment : Fragment() {
    private var recyclerView: RecyclerView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_user_search, container, false)


        recyclerView = view.findViewById(R.id.user_search_recyclerView)
        var userList = ArrayList<User>()
        var userItemAdapter = context?.let { UserItemAdapter(it, userList as ArrayList<User>, true) }
        recyclerView?.setHasFixedSize(true)
        recyclerView?.layoutManager = LinearLayoutManager(context)
        recyclerView?.adapter = userItemAdapter

        Log.d("TAG002", "bbbb")

        val usersRef = Firebase.firestore.collection("users")
//            .orderBy("fullname")
        usersRef.get().addOnSuccessListener { result ->
            Log.d("TAG002", "bbbb")

            userList?.clear()
            for (document in result) {
                Log.d("TAG22222", "${document.id} => ${document.data}")
                val user = document.toObject<User>()
                if (user != null)
                {
                    userList?.add(user)
                }
            }
            userItemAdapter?.notifyDataSetChanged()
        }
            .addOnFailureListener { exception ->
                Log.d("TAG22222", "Error getting documents: ", exception)
            }


        return view
    }

}