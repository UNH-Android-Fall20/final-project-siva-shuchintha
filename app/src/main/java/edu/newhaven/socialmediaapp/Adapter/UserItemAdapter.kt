package edu.newhaven.socialmediaapp.Adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import edu.newhaven.socialmediaapp.R
import edu.newhaven.socialmediaapp.models.User

class UserItemAdapter (private var context: Context,
                       private var userList: List<User>,
                       private var isFragment: Boolean = false) : RecyclerView.Adapter<UserItemAdapter.ViewHolder>()
{
    private var firebaseUser: FirebaseUser? = FirebaseAuth.getInstance().currentUser

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserItemAdapter.ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.user_card_search, parent, false)
        Log.d("TAG333", "cchellocc")

        return UserItemAdapter.ViewHolder(view)
    }

    override fun getItemCount(): Int {
        Log.d("TAG333", userList.size.toString())

        return userList.size
    }
    override fun onBindViewHolder(holder: UserItemAdapter.ViewHolder, position: Int) {

        val userItem = userList[position]
        Log.d("TAG333", "ccbindcc")

        holder.usernameOtherUserText.text = userItem.username
        holder.followOtherUserButton.setOnClickListener { Log.d("TAG1","here in onbind follow") }
    }

    class ViewHolder (@NonNull itemView: View) : RecyclerView.ViewHolder(itemView)
    {
        var usernameOtherUserText: TextView = itemView.findViewById(R.id.other_username_textView)
        var followOtherUserButton: Button = itemView.findViewById(R.id.follow_otheruser_button)
    }
}