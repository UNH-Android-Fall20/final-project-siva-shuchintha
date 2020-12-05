package edu.newhaven.socialmediaapp.Fragments


import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import edu.newhaven.socialmediaapp.Adapter.UserItemAdapter
import edu.newhaven.socialmediaapp.models.User
import edu.newhaven.socialmediaapp.R
import kotlinx.android.synthetic.main.fragment_user_search.view.*


class UserSearchFragment : Fragment() {
    private var recyclerView: RecyclerView? = null
    private var userItemAdapter: UserItemAdapter? = null
    private lateinit var CurrentUser: FirebaseUser
    private var userList: MutableList<User>? = null

//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        arguments?.let {
//        }
//    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_user_search, container, false)

        CurrentUser = FirebaseAuth.getInstance().currentUser!!
        recyclerView = view.findViewById(R.id.user_search_recyclerView)
        recyclerView?.setHasFixedSize(true)
        recyclerView?.layoutManager = LinearLayoutManager(context)


        userList = ArrayList()
        userItemAdapter = context?.let { UserItemAdapter(it, userList as ArrayList<User>, true) }
        recyclerView?.adapter = userItemAdapter
        view.user_search_editText.addTextChangedListener(object: TextWatcher
        {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int){}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int)
            {
                    recyclerView?.visibility = View.VISIBLE
                    getUserList()
                    searchOtherUsers(s.toString().toLowerCase())
            }
            override fun afterTextChanged(s: Editable?){}
        })


        return view
    }



    private fun searchOtherUsers(input: String)
    {
        val query = Firebase.firestore.collection("users")
            .orderBy("fullname")
            .startAt(input)
            .endAt(input + "\uf8ff")
        Log.d("TAG001", "aaaaaaa")
        Log.d("TAG001", "aaaaaaaffffff")

        query.get().addOnSuccessListener { result ->
            for (document in result) {
                Log.d("TAG11111", "${document.id} => ${document.data}")
                userList?.clear()
                for (document in result) {
                    Log.d("TAG11111", "${document.id} => ${document.data}")
                    val otherUser = document.toObject<User>()


                    if (otherUser != null && otherUser.uid != CurrentUser.uid)
                    {
                        userList?.add(otherUser)
                    }
                }
                userItemAdapter?.notifyDataSetChanged()
            }
        }
            .addOnFailureListener { exception ->
                Log.d("TAG11111", "Error getting documents: ", exception)
            }
    }


    private fun getUserList() {
        Log.d("TAG002", "bbbb")

        val usersRef = Firebase.firestore.collection("users")
//            .orderBy("fullname")
        usersRef.get().addOnSuccessListener { result ->
            Log.d("TAG002", "bbbb")
            userList?.clear()
            for (document in result) {
                Log.d("TAG22222", "${document.id} => ${document.data}")
                val otherUser = document.toObject<User>()
                if (otherUser != null && otherUser.uid != CurrentUser.uid)
                {
                    userList?.add(otherUser)
                }
            }
            userItemAdapter?.notifyDataSetChanged()
        }
            .addOnFailureListener { exception ->
                Log.d("TAG22222", "Error getting documents: ", exception)
            }
    }
}
